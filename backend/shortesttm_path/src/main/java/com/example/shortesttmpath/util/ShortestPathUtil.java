package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.InvalidLineException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;

/**
 * Util class to calculate the shortest metro path (the one with the least stations) between two STM
 * metro stations.
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
  private static final Map<Integer, Map<Integer, Integer>> MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M;

  static {
    try {
      MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M = getMapScrToMapDestinationToDistanceInM();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public static List<String> getAllStationsInAlphabeticalOrder() {
    List<String> allStationsInAlphabeticalOrder =
        new ArrayList<>(STATIONS_NAMES_TO_INTS.keySet().stream().toList());
    Collator collator = Collator.getInstance(Locale.FRENCH);
    allStationsInAlphabeticalOrder.sort(collator);

    return allStationsInAlphabeticalOrder;
  }

  /**
   * Returns all the stations for a metro line.
   *
   * @param line The metro line.
   * @return All the stations for the metro line.
   */
  public static List<String> getAllStations(String line) {
    List<Line> values = Arrays.stream(Line.values()).toList();
    List<String> stringValues = values.stream().map(Enum::toString).toList();

    if (!stringValues.contains(line)) {
      throw new InvalidLineException();
    }

    Line enumLine = Line.valueOf(line);

    return switch (enumLine) {
      case Line.BLUE -> BLUE_LINE_STATIONS;
      case Line.GREEN -> GREEN_LINE_STATIONS;
      case Line.ORANGE -> ORANGE_LINE_STATIONS;
      default -> YELLOW_LINE_STATIONS;
    };
  }

  private static Map<Integer, Map<Integer, Integer>> getMapScrToMapDestinationToDistanceInM()
      throws IOException {
    Map<Integer, Map<Integer, Integer>> distancesMap = new LinkedHashMap<>();
    ClassPathResource resource = new ClassPathResource("static/distances");
    List<String> distancesLines =
        new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();

    for (String distanceLine : distancesLines) {
      addDistance(distanceLine, distancesMap);
    }

    return distancesMap;
  }

  private static void addDistance(String distanceLine,
                                  Map<Integer, Map<Integer, Integer>> distancesMap) {
    int station1 = STATIONS_NAMES_TO_INTS.get(distanceLine.split(" to ")[0]);
    int station2 = STATIONS_NAMES_TO_INTS.get(distanceLine.split(" to ")[0].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[0].split("\\s:\\s")[1]);

    if (distancesMap.containsKey(station1)) {
      distancesMap.get(station1).put(station2, distance);
    } else {
      distancesMap.put(station1, Map.of(station2, distance));
    }
  }

  /**
   * Returns the shortest metro path (the one with the least stations) between two STM metro
   * stations. A StationsOnSameLineException exception is thrown if the two stations are on the
   * same line. This includes the same station given twice and neighbor stations. Source:
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
    dijkstra(GRAPH, start);
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(startingStation);
    shortestPath.setDestinationStation(destinationStation);
    List<String> stationsToSwitchLines = new ArrayList<>();
    List<String> allStations = new ArrayList<>();
    List<String> stationsToExclude = getStationsToExclude(stationsToSwitchLines, allStations);
    stationsToSwitchLines.removeAll(stationsToExclude);
    shortestPath.setStationsToSwitchLines(stationsToSwitchLines);
    return shortestPath;
  }

  // Source: https://www.geeksforgeeks.org/dsa/dijkstras-shortest-path-algorithm-greedy-algo-7/
  private static ArrayList<Integer> dijkstra(List<List<int[]>> adj, int src) {
    int V = adj.size();

    // Min-heap (priority queue) storing pairs of (distance, node)
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

    // Distance array: stores shortest distance from source
    int[] dist = new int[V];
    Arrays.fill(dist, Integer.MAX_VALUE);

    // Distance from source to itself is 0
    dist[src] = 0;
    pq.offer(new int[]{0, src});

    // Process the queue until all reachable vertices are finalized
    while (!pq.isEmpty()) {
      int[] top = pq.poll();
      int d = top[0];
      int u = top[1];

      // If this distance is not the latest shortest one, skip it
      if (d > dist[u])
        continue;

      // Explore all adjacent vertices
      for (int[] p : adj.get(u)) {
        int v = p[0];
        int w = p[1];

        // If we found a shorter path to v through u, update it
        if (dist[u] + w < dist[v]) {
          dist[v] = dist[u] + w;
          pq.offer(new int[]{dist[v], v});
        }
      }
    }

    ArrayList<Integer> result = new ArrayList<>();
    for (int d : dist)
      result.add(d);

    // Return the final shortest distances from the source
    return result;
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
