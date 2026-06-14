package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;

/**
 * Util class to calculate the shortest metro path (the one with the least stations) between two
 * STM metro stations.
 */
public class ShortestPathUtil {
  private static final List<String> LINES_FILES_NAMES = Arrays.asList(
      "blue_line_stations.txt",
      "green_line_stations.txt",
      "orange_line_stations.txt",
      "yellow_line_stations.txt"
  );
  private static final int NUMBER_OF_VERTICES = 68;
  private static final List<List<Integer>> GRAPH;
  private static final Map<String, Integer> STATIONS_NAMES_TO_INTS;

  static {
    try {
      STATIONS_NAMES_TO_INTS = getStationsNamesToInts();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      GRAPH = getGraph();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final Map<Integer, String> INTS_TO_STATIONS_NAMES = getIntsToStationsNames();
  private static final List<String> ALL_STATIONS_TO_SWITCH_LINES = Arrays.asList(
      "Berri-UQAM",
      "Lionel-Groulx",
      "Snowdon",
      "Jean-Talon"
  );
  private static List<String> BLUE_LINE_STATIONS;
  private static List<String> GREEN_LINE_STATIONS;
  private static List<String> ORANGE_LINE_STATIONS;
  private static List<String> YELLOW_LINE_STATIONS;

  /**
   * The default constructor.
   */
  public ShortestPathUtil() {
  }

  /**
   * Returns the shortest metro path (the one with the least stations) between two STM metro
   * stations. A
   * StationsOnSameLineException exception is thrown if the two stations are on the same line.
   * This includes the same
   * station given twice and neighbor stations.
   * Source:
   * <a href="https://www.geeksforgeeks.org/dsa/shortest-path-unweighted-graph/">https://www.geeksforgeeks.org/dsa/shortest-path-unweighted-graph/</a>
   *
   * @param startingStation    The starting station.
   * @param destinationStation The destination station.
   * @return The shortest metro path (the one with the least stations) between two STM metro
   *     stations.
   */
  public static ShortestPathBean getShortestPath(String startingStation,
                                                 String destinationStation) {
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
    int start = STATIONS_NAMES_TO_INTS.get(startingStation);
    int destination = STATIONS_NAMES_TO_INTS.get(destinationStation);
    // par[] array stores the parent of nodes
    List<Integer> par
        = new ArrayList<>(Collections.nCopies(NUMBER_OF_VERTICES, -1));
    // dist[] array stores the distance of nodes from S
    List<Integer> dist = new ArrayList<>(
        Collections.nCopies(NUMBER_OF_VERTICES, Integer.MAX_VALUE));
    // Function call to find the distance of all nodes
    // and their parent nodes
    bfs(GRAPH, start, par, dist);
    // List path stores the shortest path
    List<Integer> path = new ArrayList<>();
    int currentNode = destination;
    path.add(destination);
    while (par.get(currentNode) != -1) {
      path.add(par.get(currentNode));
      currentNode = par.get(currentNode);
    }
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(startingStation);
    shortestPath.setDestinationStation(destinationStation);
    List<String> stationsToSwitchLines = new ArrayList<>();
    List<String> allStations = new ArrayList<>();
    for (int i = path.size() - 1; i >= 0; i--) {
      String stationName = INTS_TO_STATIONS_NAMES.get(path.get(i));
      allStations.add(stationName);
      if (ALL_STATIONS_TO_SWITCH_LINES.contains(stationName)
          && !stationName.equals(startingStation) && !stationName.equals(destinationStation)) {
        stationsToSwitchLines.add(stationName);
      }
    }
    List<String> stationsToExclude = getStationsToExclude(stationsToSwitchLines, allStations);
    stationsToSwitchLines.removeAll(stationsToExclude);
    shortestPath.setStationsToSwitchLines(stationsToSwitchLines);
    return shortestPath;
  }

  private static boolean areStationsOnTheSameLine(String startingStation,
                                                  String destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private static List<String> getStationsToExclude(List<String> stationsToSwitchLines,
                                                   List<String> allStations) {
    List<String> stationsToExclude = new ArrayList<>();
    for (String station : stationsToSwitchLines) {
      int indexOfStationInAllStations = allStations.indexOf(station);
      String stationBefore = allStations.get(indexOfStationInAllStations - 1);
      String stationAfter = allStations.get(indexOfStationInAllStations + 1);
      if (getLines(stationBefore).getFirst().equals(getLines(stationAfter).getFirst())
          && getLines(station).contains(getLines(stationBefore).getFirst())) {
        stationsToExclude.add(station);
      }
    }
    return stationsToExclude;
  }

  private static List<String> getLines(String station) {
    List<String> lines = new ArrayList<>();
    if (BLUE_LINE_STATIONS.contains(station)) {
      lines.add("BLUE");
    }
    if (GREEN_LINE_STATIONS.contains(station)) {
      lines.add("GREEN");
    }
    if (ORANGE_LINE_STATIONS.contains(station)) {
      lines.add("ORANGE");
    }
    if (YELLOW_LINE_STATIONS.contains(station)) {
      lines.add("YELLOW");
    }
    return lines;
  }

  // Source: https://www.geeksforgeeks.org/dsa/shortest-path-unweighted-graph/
  // Modified bfs to store the parent of nodes along with
  // the distance from the source node
  private static void bfs(List<List<Integer>> graph, int start,
                          List<Integer> par, List<Integer> dist) {
    // Queue to store the nodes in the order they are
    // visited
    Queue<Integer> q = new LinkedList<>();
    // Mark the distance of the source node as 0
    dist.set(start, 0);
    // Push the source node to the queue
    q.add(start);
    // Iterate until the queue is not empty
    while (!q.isEmpty()) {
      // Pop the node at the front of the queue
      int node = q.poll();
      // Explore all the neighbors of the current node
      for (int neighbor : graph.get(node)) {
        // Check if the neighboring node is not
        // visited
        if (dist.get(neighbor)
            == Integer.MAX_VALUE) {
          // Mark the current node as the parent
          // of the neighboring node
          par.set(neighbor, node);
          // Mark the distance of the neighboring
          // node as the distance of the current
          // node + 1
          dist.set(neighbor, dist.get(node) + 1);
          // Insert the neighboring node to the
          // queue
          q.add(neighbor);
        }
      }
    }
  }

  private static Map<Integer, String> getIntsToStationsNames() {
    Map<Integer, String> intsToStationsNames = new LinkedHashMap<>();
    for (String station : STATIONS_NAMES_TO_INTS.keySet()) {
      intsToStationsNames.put(STATIONS_NAMES_TO_INTS.get(station), station);
    }
    return intsToStationsNames;
  }

  private static Map<String, Integer> getStationsNamesToInts() throws IOException {
    Map<String, Integer> stationNamesToInts = new LinkedHashMap<>();
    Set<String> uniqueStationsNames = getUniqueStationsNames();
    int i = 0;
    for (String stationName : uniqueStationsNames) {
      stationNamesToInts.put(stationName, i);
      i++;
    }
    return stationNamesToInts;
  }

  private static Set<String> getUniqueStationsNames() throws IOException {
    Set<String> uniqueStationsNames = new LinkedHashSet<>();
    for (String lineFileName : LINES_FILES_NAMES) {
      addLineToUniqueStationsNames(uniqueStationsNames, lineFileName);
    }
    return uniqueStationsNames;
  }

  private static void addLineToUniqueStationsNames(Set<String> uniqueStationsNames,
                                                   String lineFileName) throws IOException {
    ClassPathResource resource = new ClassPathResource("static/" + lineFileName);
    List<String> stations =
        new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
    switch (lineFileName) {
      case "blue_line_stations.txt":
        BLUE_LINE_STATIONS = new ArrayList<>(stations);
        break;
      case "green_line_stations.txt":
        GREEN_LINE_STATIONS = new ArrayList<>(stations);
        break;
      case "orange_line_stations.txt":
        ORANGE_LINE_STATIONS = new ArrayList<>(stations);
        break;
      default:
        YELLOW_LINE_STATIONS = new ArrayList<>(stations);
    }
    uniqueStationsNames.addAll(stations);
  }

  private static List<List<Integer>> getGraph() throws IOException {
    List<List<Integer>> graph = new ArrayList<>(NUMBER_OF_VERTICES);
    for (String lineFileName : LINES_FILES_NAMES) {
      addLineToGraph(graph, lineFileName);
    }
    List<List<Integer>> graphAsAdjencyList = new ArrayList<>(NUMBER_OF_VERTICES);
    for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
      graphAsAdjencyList.add(new ArrayList<>());
    }
    for (List<Integer> edge : graph) {
      graphAsAdjencyList.get(edge.get(0)).add(edge.get(1));
      graphAsAdjencyList.get(edge.get(1)).add(edge.get(0));
    }
    return graphAsAdjencyList;
  }

  private static void addLineToGraph(List<List<Integer>> graph, String lineFileName)
      throws IOException {
    ClassPathResource resource = new ClassPathResource("static/" + lineFileName);
    List<String> stations =
        new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
    for (int i = 0; i < stations.size() - 1; i++) {
      addTwoStationsInBothDirections(graph, STATIONS_NAMES_TO_INTS.get(stations.get(i)),
          STATIONS_NAMES_TO_INTS.get(stations.get(i + 1)));
    }
  }

  private static void addTwoStationsInBothDirections(List<List<Integer>> graph, Integer station1,
                                                     Integer station2) {
    List<Integer> edgeInFirstDirection = Arrays.asList(
        station1,
        station2
    );
    List<Integer> edgeInSecondDirection = Arrays.asList(
        station2,
        station1
    );
    graph.add(edgeInFirstDirection);
    graph.add(edgeInSecondDirection);
  }
}
