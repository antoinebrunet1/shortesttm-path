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
  private static ShortestPathUtil single_instance = null;
  private  List<String> blueLineStations;
  private  List<String> greenLineStations;
  private  List<String> orangeLineStations;
  private  List<String> yellowLineStations;
  private  final List<List<int[]>> GRAPH;
  private  final Map<String, Integer> STATIONS_NAMES_TO_INTS;
  private  final Map<Integer, Map<Integer, Integer>>
      MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M;
  private  final Map<Integer, String> INTS_TO_STATIONS_NAMES;
  private  final List<String> ALL_STATIONS_TO_SWITCH_LINES = Arrays.asList(
      "Berri-UQAM",
      "Lionel-Groulx",
      "Snowdon",
      "Jean-Talon"
  );
  private  final Map<Line, List<String>> LINES_TO_STATIONS;

  private ShortestPathUtil() throws IOException {
    fillStations();
    GRAPH = getGraph();
    STATIONS_NAMES_TO_INTS = getStationsNamesToInts();
    MAP_SRC_TO_MAP_DESTINATION_TO_DISTANCE_IN_M = getMapScrToMapDestinationToDistanceInM();
    INTS_TO_STATIONS_NAMES = getIntsToStationsNames();
    LINES_TO_STATIONS = Map.of(
        Line.BLUE, blueLineStations,
        Line.GREEN, greenLineStations,
        Line.ORANGE, orangeLineStations,
        Line.YELLOW, yellowLineStations
    );
  }

  public static synchronized ShortestPathUtil getInstance() throws IOException {
    if (single_instance == null)
      single_instance = new ShortestPathUtil();

    return single_instance;
  }

  private void fillStations() throws IOException {
    blueLineStations = getStationsForLineFileName("blue_line_stations.txt");
    greenLineStations = getStationsForLineFileName("green_line_stations.txt");
    orangeLineStations = getStationsForLineFileName("orange_line_stations.txt");
    yellowLineStations = getStationsForLineFileName("yellow_line_stations.txt");
  }

  private List<String> getStationsForLineFileName(String lineFileName) throws IOException {
    ClassPathResource resource = new ClassPathResource("static/" + lineFileName);
    return new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public  List<String> getAllStationsInAlphabeticalOrder() {
    List<String> allStationsInAlphabeticalOrder =
        new ArrayList<>(STATIONS_NAMES_TO_INTS.keySet().stream().toList());
    Collator collator = Collator.getInstance(Locale.FRENCH);
    allStationsInAlphabeticalOrder.sort(collator);

    return allStationsInAlphabeticalOrder;
  }

  private  String getDirectionOfStation(String station1, String station2) {
    List<String> station1Lines = getLines(station1);
    List<String> station2Lines = getLines(station2);
    String lineOfDirection = getLineOfDirection(station1Lines, station2Lines);
    List<String> allStationsOfLineOfDirection = getAllStationsOfLineOfDirection(lineOfDirection);
    int indexOfStation1OnLine = allStationsOfLineOfDirection.indexOf(station1);
    int indexOfStation2OnLine = allStationsOfLineOfDirection.indexOf(station2);
    List<String> stationsOfLineOfDirection = LINES_TO_STATIONS.get(Line.valueOf(lineOfDirection));
    List<String> directions = List.of(stationsOfLineOfDirection.getFirst(),
        stationsOfLineOfDirection.getLast());

    return indexOfStation1OnLine < indexOfStation2OnLine ? directions.getLast() :
        directions.getFirst();
  }

  private  String getLineOfDirection(List<String> station1Lines, List<String> station2Lines) {
    return station1Lines.stream()
        .distinct()
        .filter(station2Lines::contains)
        .collect(Collectors.toSet()).iterator().next();
  }

  private  List<String> getAllStationsOfLineOfDirection(String lineOfDirection) {
    return LINES_TO_STATIONS.get(Line.valueOf(lineOfDirection));
  }

  private  Map<Integer, Map<Integer, Integer>> getMapScrToMapDestinationToDistanceInM()
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

  private  void addDistance(String distanceLine,
                                  Map<Integer, Map<Integer, Integer>> distancesMap) {
    int station1 = STATIONS_NAMES_TO_INTS.get(distanceLine.split(" to ")[0]);
    int station2 = STATIONS_NAMES_TO_INTS.get(distanceLine.split(" to ")[1].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[1].split("\\s:\\s")[1]);

    addDistance(station1, station2, distance, distancesMap);
    addDistance(station2, station1, distance, distancesMap);
  }

  private  void addDistance(int station1, int station2, int distance,
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
  public  ShortestPathBean getShortestPath(String startingStation,
                                                 String destinationStation) {
    validateStations(startingStation, destinationStation);
    int start = STATIONS_NAMES_TO_INTS.get(startingStation);
    int target = STATIONS_NAMES_TO_INTS.get(destinationStation);
    List<String> allStations = getPathStations(start, target);

    return getShortestPathBean(startingStation, destinationStation, allStations);
  }

  private  void validateStations(String startingStation, String destinationStation) {
    if (!areInputStationsValid(startingStation, destinationStation)) {
      throw new StationsNotValidException();
    }
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
  }

  private  List<String> getPathStations(int start, int target) {
    return DijkstraUtil.dijkstra(GRAPH, start, target)
        .stream()
        .map(INTS_TO_STATIONS_NAMES::get)
        .toList();
  }

  private  ShortestPathBean getShortestPathBean(String startingStation,
                                                      String destinationStation,
                                                      List<String> allStations) {
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

  private  List<NonEndingStationInPathBean> getStationsToSwitchLinesObjects(
      List<String> stationsToSwitchLines, List<String> allStations) {
    List<NonEndingStationInPathBean> stationsToSwitchLinesObjects = new ArrayList<>();

    for (String station : stationsToSwitchLines) {
      NonEndingStationInPathBean stationObject = getStationObject(station, allStations);

      stationsToSwitchLinesObjects.add(stationObject);
    }

    return stationsToSwitchLinesObjects;
  }

  private  NonEndingStationInPathBean getStationObject(String station,
                                                             List<String> allStations) {
    String nextStation = allStations.get(allStations.indexOf(station) + 1);
    String direction = getDirectionOfStation(station, nextStation);
    String line = getLines(direction).getFirst();

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private  List<String> getStationsToSwitchLines(List<String> allStations,
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

  private  boolean areStationsOnTheSameLine(String startingStation,
                                                  String destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private  boolean areInputStationsValid(String startingStation, String destinationStation) {
    return STATIONS_NAMES_TO_INTS.containsKey(startingStation)
        && STATIONS_NAMES_TO_INTS.containsKey(destinationStation);
  }

  private  List<String> getStationsToExclude(List<String> stationsToSwitchLines,
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

  private  List<String> getLines(String station) {
    List<String> lines = new ArrayList<>();

    for (Line line : LINES_TO_STATIONS.keySet()) {
      if (LINES_TO_STATIONS.get(line).contains(station)) {
        lines.add(line.name());
      }
    }

    return lines;
  }

  private  Map<Integer, String> getIntsToStationsNames() {
    Map<Integer, String> intsToStationsNames = new LinkedHashMap<>();
    for (String station : STATIONS_NAMES_TO_INTS.keySet()) {
      intsToStationsNames.put(STATIONS_NAMES_TO_INTS.get(station), station);
    }
    return intsToStationsNames;
  }

  private  Map<String, Integer> getStationsNamesToInts() {
    Map<String, Integer> stationNamesToInts = new LinkedHashMap<>();
    List<String> uniqueStationsNames = new ArrayList<>(getUniqueStationsNames());
    for (int i = 0; i < uniqueStationsNames.size(); i++) {
      stationNamesToInts.put(uniqueStationsNames.get(i), i);
    }
    return stationNamesToInts;
  }

  private  Set<String> getUniqueStationsNames() {
    Set<String> uniqueStationsNames = new LinkedHashSet<>();
    uniqueStationsNames.addAll(blueLineStations);
    uniqueStationsNames.addAll(greenLineStations);
    uniqueStationsNames.addAll(orangeLineStations);
    uniqueStationsNames.addAll(yellowLineStations);

    return uniqueStationsNames;
  }

  private  List<List<int[]>> getGraph() {
    int numberOfVertices = 68;
    List<List<int[]>> graph = new ArrayList<>(numberOfVertices);
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
