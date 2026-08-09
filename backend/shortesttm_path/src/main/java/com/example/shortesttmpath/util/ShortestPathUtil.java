package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
  private static final Map<Line, List<String>> LINES_TO_STATIONS = Map.of(
      Line.BLUE, BLUE_LINE_STATIONS,
      Line.GREEN, GREEN_LINE_STATIONS,
      Line.ORANGE, ORANGE_LINE_STATIONS,
      Line.YELLOW, YELLOW_LINE_STATIONS
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

  private static String getDirectionOfStation(String station1, String station2) {
    List<String> station1Lines = getLines(station1);
    List<String> station2Lines = getLines(station2);
    String lineOfDirection = getLineOfDirection(station1Lines, station2Lines);
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
    return LINES_TO_STATIONS.get(Line.valueOf(lineOfDirection));
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
    validateStations(startingStation, destinationStation);
    int start = STATIONS_NAMES_TO_INTS.get(startingStation);
    int destination = STATIONS_NAMES_TO_INTS.get(destinationStation);
    List<String> allStations =
        DijkstraUtil.dijkstra(GRAPH, start, destination)
            .stream()
            .map(INTS_TO_STATIONS_NAMES::get)
            .toList();
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(getStationObject(startingStation, allStations));
    shortestPath.setDestinationStation(destinationStation);
    List<String> stationsToSwitchLines =
        getStationsToSwitchLines(allStations, startingStation, destinationStation);
    List<String> stationsToExclude = getStationsToExclude(stationsToSwitchLines, allStations);
    stationsToSwitchLines.removeAll(stationsToExclude);
    List<NonEndingStationInPathBean> stationsToSwitchLinesObjects = getStationsToSwitchLinesObjects(
        stationsToSwitchLines, allStations);
    shortestPath.setStationsToSwitchLines(stationsToSwitchLinesObjects);
    return shortestPath;
  }

  private static void validateStations(String startingStation, String destinationStation) {
    if (!areInputStationsValid(startingStation, destinationStation)) {
      throw new StationsNotValidException();
    }
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
  }

  private static List<NonEndingStationInPathBean> getStationsToSwitchLinesObjects(
      List<String> stationsToSwitchLines, List<String> allStations) {
    List<NonEndingStationInPathBean> stationsToSwitchLinesObjects = new ArrayList<>();

    for (String station : stationsToSwitchLines) {
      NonEndingStationInPathBean stationObject = getStationObject(station, allStations);

      stationsToSwitchLinesObjects.add(stationObject);
    }

    return stationsToSwitchLinesObjects;
  }

  private static NonEndingStationInPathBean getStationObject(String station,
                                                             List<String> allStations) {
    String nextStation = allStations.get(allStations.indexOf(station) + 1);
    String direction = getDirectionOfStation(station, nextStation);
    String line = getLines(direction).getFirst();

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private static List<String> getStationsToSwitchLines(List<String> allStations,
                                                       String startingStation,
                                                       String destinationStation) {
    List<String> stationsToSwitchLines = new LinkedList<>();

    for (String station : allStations) {
      if (ALL_STATIONS_TO_SWITCH_LINES.contains(station)
          && !station.equals(startingStation) && !station.equals(destinationStation)) {
        stationsToSwitchLines.add(station);
      }
    }

    return stationsToSwitchLines;
  }

  private static boolean areStationsOnTheSameLine(String startingStation,
                                                  String destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private static boolean areInputStationsValid(String startingStation, String destinationStation) {
    return STATIONS_NAMES_TO_INTS.containsKey(startingStation)
        && STATIONS_NAMES_TO_INTS.containsKey(destinationStation);
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

    for (Line line : LINES_TO_STATIONS.keySet()) {
      if (LINES_TO_STATIONS.get(line).contains(station)) {
        lines.add(line.name());
      }
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
    List<String> uniqueStationsNames = new ArrayList<>(getUniqueStationsNames());
    for (int i = 0; i < uniqueStationsNames.size(); i++) {
      stationNamesToInts.put(uniqueStationsNames.get(i), i);
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
