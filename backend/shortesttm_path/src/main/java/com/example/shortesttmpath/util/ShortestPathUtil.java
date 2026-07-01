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
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

/**
 * Util class to calculate the shortest metro path between two STM metro stations.
 */
public class ShortestPathUtil {
  private static final List<String> LINES_FILES_NAMES = Arrays.asList(
      "blue_line_stations.txt",
      "green_line_stations.txt",
      "orange_line_stations.txt",
      "yellow_line_stations.txt"
  );
  private static final int NUMBER_OF_VERTICES = 68;
  private static final List<List<int[]>> GRAPH;
  private static final Map<String, Integer> STATIONS_NAMES_TO_INTS;
  private static final Map<Integer, Map<Integer, Integer>>
      MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M;

  static {
    try {
      STATIONS_NAMES_TO_INTS = getStationsNamesToInts();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M = getMapScrToMapDestinationToDistanceInM();
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
  private static final Map<Line, List<String>> LINES_TO_DIRECTIONS = Map.of(
      Line.BLUE, List.of(BLUE_LINE_STATIONS.getFirst(), BLUE_LINE_STATIONS.getLast()),
      Line.GREEN, List.of(GREEN_LINE_STATIONS.getFirst(), GREEN_LINE_STATIONS.getLast()),
      Line.ORANGE, List.of(ORANGE_LINE_STATIONS.getFirst(), ORANGE_LINE_STATIONS.getLast()),
      Line.YELLOW, List.of(YELLOW_LINE_STATIONS.getFirst(), YELLOW_LINE_STATIONS.getLast())
  );

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

  private static String getDirectionOfStartingStation(List<String> allStations) {
    String station1 = allStations.getFirst();
    String station2 = allStations.get(1);
    List<String> station1Lines = getLines(station1);
    List<String> station2Lines = getLines(station2);
    String lineOfDirection = getLineOfDirection(station1Lines, station2Lines);
    List<String> directionsOfLineOfDirection =
        LINES_TO_DIRECTIONS.get(Line.valueOf(lineOfDirection));
    List<String> allStationsOfLineOfDirection = getAllStationsOfLineOfDirection(lineOfDirection);
    int indexOfStation1OnLine = allStationsOfLineOfDirection.indexOf(station1);
    int indexOfStation2OnLine = allStationsOfLineOfDirection.indexOf(station2);
    List<String> directions = LINES_TO_DIRECTIONS.get(Line.valueOf(lineOfDirection));

    return indexOfStation1OnLine < indexOfStation2OnLine ? directions.getLast() :
        directions.getFirst();
  }

  private static String getLineOfDirection(List<String> station1Lines, List<String> station2Lines) {
    return station1Lines.stream()
        .distinct()
        .filter(station2Lines::contains)
        .collect(Collectors.toSet()).iterator().next();
  }

  private static List<String> getAllStationsOfLineOfDirection(String lineOfDirection) {
    switch (Line.valueOf(lineOfDirection)) {
      case Line.BLUE:
        return BLUE_LINE_STATIONS;
      case Line.GREEN:
        return GREEN_LINE_STATIONS;
      case Line.ORANGE:
        return ORANGE_LINE_STATIONS;
      default:
        return YELLOW_LINE_STATIONS;
    }
  }

  private static Map<Integer, Map<Integer, Integer>> getMapScrToMapDestinationToDistanceInM()
      throws IOException {
    Map<Integer, Map<Integer, Integer>> distancesMap = new LinkedHashMap<>();
    ClassPathResource resource = new ClassPathResource("static/distances.txt");
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
    int station2 = STATIONS_NAMES_TO_INTS.get(distanceLine.split(" to ")[1].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[1].split("\\s:\\s")[1]);

    addDistance(station1, station2, distance, distancesMap);
    addDistance(station2, station1, distance, distancesMap);
  }

  private static void addDistance(int station1, int station2, int distance,
                                  Map<Integer, Map<Integer, Integer>> distancesMap) {
    if (distancesMap.containsKey(station1)) {
      distancesMap.get(station1).put(station2, distance);
    } else {
      Map<Integer, Integer> station1Map = new LinkedHashMap<>();
      station1Map.put(station2, distance);
      distancesMap.put(station1, station1Map);
    }
  }

  /**
   * Returns the shortest metro path between two STM metro stations. A StationsOnSameLineException
   * exception is thrown if the two stations are on the same line. This includes the same station
   * given twice and neighbor stations.
   *
   * @param startingStation    The starting station.
   * @param destinationStation The destination station.
   * @return The shortest metro path between two STM metro stations.
   */
  public static ShortestPathBean getShortestPath(String startingStation,
                                                 String destinationStation) {
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
    int start = STATIONS_NAMES_TO_INTS.get(startingStation);
    int destination = STATIONS_NAMES_TO_INTS.get(destinationStation);
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(startingStation);
    shortestPath.setDestinationStation(destinationStation);
    List<String> allStations =
        dijkstra(start, destination).stream().map(INTS_TO_STATIONS_NAMES::get).toList();
    List<String> stationsToSwitchLines =
        getStationsToSwitchLines(allStations, startingStation, destinationStation);
    List<String> stationsToExclude = getStationsToExclude(stationsToSwitchLines, allStations);
    stationsToSwitchLines.removeAll(stationsToExclude);
    shortestPath.setStationsToSwitchLines(stationsToSwitchLines);
    return shortestPath;
  }

  private static List<String> getStationsToSwitchLines(List<String> allStations,
                                                       String startingStation,
                                                       String destinationStation) {
    List<String> stationsToSwitchLines = new ArrayList<>();

    for (String station : allStations) {
      if (ALL_STATIONS_TO_SWITCH_LINES.contains(station)
          && !station.equals(startingStation) && !station.equals(destinationStation)) {
        stationsToSwitchLines.add(station);
      }
    }

    return stationsToSwitchLines;
  }

  /**
   * Dijkstra's algorithm to find the shortest path
   * Source: <a href="https://medium.com/@robinviktorsson/dijkstras-algorithm-in-java-learn-with-practical-examples-9e7af310e466">...</a>.
   *
   * @param start The starting station as an int.
   * @param target The destination station as an int.
   * @return The stations of the path as ints.
   */
  private static List<Integer> dijkstra(
      int start,
      int target) {

    // Number of nodes in the graph
    int n = NUMBER_OF_VERTICES;

    // Stores shortest known distance from start node
    int[] dist = new int[n];

    // Stores previous node for path reconstruction
    int[] parent = new int[n];

    // Initialize all distances to infinity
    Arrays.fill(dist, Integer.MAX_VALUE);

    // Initialize parents as undefined
    Arrays.fill(parent, -1);

    // Distance to start node is 0
    dist[start] = 0;

    // Min-heap priority queue:
    // each element = {distance, node}
    PriorityQueue<int[]> pq =
        new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    // Start with the source node
    pq.offer(new int[]{0, start});

    // Process nodes until queue is empty
    while (!pq.isEmpty()) {

      // Get node with smallest distance
      int[] curr = pq.poll();

      int d = curr[0];
      int node = curr[1];

      // Skip outdated queue entries
      if (d > dist[node]) {
        continue;
      }

      // Explore all neighbors of current node
      for (int[] edge : GRAPH.get(node)) {

        int neighbor = edge[0];

        // Calculate new possible distance
        int newDist = dist[node] + edge[1];

        // If a shorter path is found
        if (newDist < dist[neighbor]) {

          // Update shortest distance
          dist[neighbor] = newDist;

          // Remember best previous node
          parent[neighbor] = node;

          // Add updated distance to priority queue
          pq.offer(new int[]{newDist, neighbor});
        }
      }
    }

    // Reconstruct shortest path
    List<Integer> path = new ArrayList<>();

    // Backtrack from target using parent array
    for (int at = target; at != -1; at = parent[at]) {
      path.add(at);
    }

    // Reverse because path was built backwards
    Collections.reverse(path);

    return path;
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

  private static List<List<int[]>> getGraph() throws IOException {
    List<List<int[]>> graph = new ArrayList<>(NUMBER_OF_VERTICES);
    Set<Integer> sortedStations1 =
        new HashSet<>(MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M.keySet());

    for (int station1 : sortedStations1) {
      Map<Integer, Integer> station1Map = MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M.get(station1);
      List<int[]> stations2 = new ArrayList<>();

      for (int station2 : station1Map.keySet()) {
        stations2.add(new int[] {station2, station1Map.get(station2)});
      }

      graph.add(stations2);
    }

    return graph;
  }
}
