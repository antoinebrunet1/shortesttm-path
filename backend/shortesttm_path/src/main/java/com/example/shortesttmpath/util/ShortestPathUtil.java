package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Util class to calculate the shortest metro path between two STM metro stations.
 */
public class ShortestPathUtil {
  private static ShortestPathUtil single_instance = null;
  private final Map<Line, List<String>> linesToStations = Map.of(
      Line.BLUE, FileUtil.getLines("blue_line_stations.txt"),
      Line.GREEN, FileUtil.getLines("green_line_stations.txt"),
      Line.ORANGE, FileUtil.getLines("orange_line_stations.txt"),
      Line.YELLOW, FileUtil.getLines("yellow_line_stations.txt")
  );
  private final List<String> allStationsToSwitchLines =
      FileUtil.getLines("all_stations_to_switch_lines.txt");
  private final Map<String, Integer> stationsNamesToInts = getStationsNamesToInts();
  private final Map<Integer, Map<Integer, Integer>>
      mapSrcToMapDestinationToDistanceInM = getMapScrToMapDestinationToDistanceInM();
  private final List<List<int[]>> graph = getGraph();
  private final Map<Integer, String> intsToStationsNames = getIntsToStationsNames();

  private ShortestPathUtil() throws IOException {
  }

  /**
   * The Singleton constructor.
   *
   * @return The Singleton.
   * @throws IOException IOException.
   */
  public static synchronized ShortestPathUtil getInstance() throws IOException {
    if (single_instance == null) {
      single_instance = new ShortestPathUtil();
    }

    return single_instance;
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public List<String> getAllStationsInAlphabeticalOrder() {
    List<String> allStationsInAlphabeticalOrder =
        new ArrayList<>(stationsNamesToInts.keySet().stream().toList());
    Collator collator = Collator.getInstance(Locale.FRENCH);
    allStationsInAlphabeticalOrder.sort(collator);

    return allStationsInAlphabeticalOrder;
  }

  private String getDirectionOfStation(String station1, String station2) {
    List<String> station1Lines = getLines(station1);
    List<String> station2Lines = getLines(station2);
    String lineOfDirection = getLineOfDirection(station1Lines, station2Lines);
    List<String> allStationsOfLineOfDirection = getAllStationsOfLineOfDirection(lineOfDirection);
    int indexOfStation1OnLine = allStationsOfLineOfDirection.indexOf(station1);
    int indexOfStation2OnLine = allStationsOfLineOfDirection.indexOf(station2);
    List<String> stationsOfLineOfDirection = linesToStations.get(Line.valueOf(lineOfDirection));
    List<String> directions = List.of(stationsOfLineOfDirection.getFirst(),
        stationsOfLineOfDirection.getLast());

    return indexOfStation1OnLine < indexOfStation2OnLine ? directions.getLast() :
        directions.getFirst();
  }

  private String getLineOfDirection(List<String> station1Lines, List<String> station2Lines) {
    return station1Lines.stream()
        .distinct()
        .filter(station2Lines::contains)
        .collect(Collectors.toSet()).iterator().next();
  }

  private List<String> getAllStationsOfLineOfDirection(String lineOfDirection) {
    return linesToStations.get(Line.valueOf(lineOfDirection));
  }

  private Map<Integer, Map<Integer, Integer>> getMapScrToMapDestinationToDistanceInM()
      throws IOException {
    Map<Integer, Map<Integer, Integer>> distancesMap = new LinkedHashMap<>();
    List<String> distancesLines = FileUtil.getLines("distances.txt");

    for (String distanceLine : distancesLines) {
      addDistance(distanceLine, distancesMap);
    }

    return distancesMap;
  }

  private void addDistance(String distanceLine,
                                  Map<Integer, Map<Integer, Integer>> distancesMap) {
    int station1 = stationsNamesToInts.get(distanceLine.split(" to ")[0]);
    int station2 = stationsNamesToInts.get(distanceLine.split(" to ")[1].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[1].split("\\s:\\s")[1]);

    addDistance(station1, station2, distance, distancesMap);
    addDistance(station2, station1, distance, distancesMap);
  }

  private void addDistance(int station1, int station2, int distance,
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
   * Returns the shortest metro path between two STM metro stations. A StationsNotValidException
   * exception is thrown if the name of at least one of the two stations is not valid. A
   * StationsOnSameLineException exception is thrown if the two stations are on the same line. This
   * includes the same station given twice and neighbor stations.
   *
   * @param startingStation    The starting station.
   * @param destinationStation The destination station.
   * @return The shortest metro path between two STM metro stations.
   */
  public ShortestPathBean getShortestPath(String startingStation,
                                                 String destinationStation) {
    validateStations(startingStation, destinationStation);
    int start = stationsNamesToInts.get(startingStation);
    int target = stationsNamesToInts.get(destinationStation);
    List<String> allStations = getPathStations(start, target);

    return getShortestPathBean(startingStation, destinationStation, allStations);
  }

  private void validateStations(String startingStation, String destinationStation) {
    if (!areInputStationsValid(startingStation, destinationStation)) {
      throw new StationsNotValidException();
    }
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
  }

  private List<String> getPathStations(int start, int target) {
    return DijkstraUtil.dijkstra(graph, start, target)
        .stream()
        .map(intsToStationsNames::get)
        .toList();
  }

  private ShortestPathBean getShortestPathBean(String startingStation,
                                                      String destinationStation,
                                                      List<String> allStations) {
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(getStationObject(startingStation, allStations));
    shortestPath.setDestinationStation(destinationStation);
    List<String> stationsToSwitchLines =
        new ArrayList<>(getStationsToSwitchLines(allStations, startingStation, destinationStation));
    List<String> stationsToExclude = getStationsToExclude(stationsToSwitchLines, allStations);
    stationsToSwitchLines.removeAll(stationsToExclude);
    List<NonEndingStationInPathBean> stationsToSwitchLinesObjects = getStationsToSwitchLinesObjects(
        stationsToSwitchLines, allStations);
    shortestPath.setStationsToSwitchLines(stationsToSwitchLinesObjects);

    return shortestPath;
  }

  private List<NonEndingStationInPathBean> getStationsToSwitchLinesObjects(
      List<String> stationsToSwitchLines, List<String> allStations) {
    List<NonEndingStationInPathBean> stationsToSwitchLinesObjects = new ArrayList<>();

    for (String station : stationsToSwitchLines) {
      NonEndingStationInPathBean stationObject = getStationObject(station, allStations);

      stationsToSwitchLinesObjects.add(stationObject);
    }

    return stationsToSwitchLinesObjects;
  }

  private NonEndingStationInPathBean getStationObject(String station,
                                                             List<String> allStations) {
    String nextStation = allStations.get(allStations.indexOf(station) + 1);
    String direction = getDirectionOfStation(station, nextStation);
    String line = getLineOfStationObject(station, direction);

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private String getLineOfStationObject(String station, String direction) {
    List<String> lines = getLines(direction);

    if (lines.size() == 1) {
      return lines.getFirst();
    }

    return lines
        .stream()
        .filter(line -> getLines(station).contains(line)
            && getLines(direction).contains(line))
        .toList()
        .getFirst();
  }

  private List<String> getStationsToSwitchLines(List<String> allStations,
                                                       String startingStation,
                                                       String destinationStation) {
    return allStations.stream().filter(station -> allStationsToSwitchLines.contains(station)
      && !List.of(startingStation, destinationStation).contains(station)).toList();
  }

  private boolean areStationsOnTheSameLine(String startingStation,
                                                  String destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private boolean areInputStationsValid(String startingStation, String destinationStation) {
    return stationsNamesToInts.containsKey(startingStation)
        && stationsNamesToInts.containsKey(destinationStation);
  }

  private List<String> getStationsToExclude(List<String> stationsToSwitchLines,
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

  private List<String> getLines(String station) {
    List<String> lines = new ArrayList<>();

    for (Line line : linesToStations.keySet()) {
      if (linesToStations.get(line).contains(station)) {
        lines.add(line.name());
      }
    }

    return lines;
  }

  private Map<Integer, String> getIntsToStationsNames() {
    Map<Integer, String> intsToStationsNames = new LinkedHashMap<>();
    for (String station : stationsNamesToInts.keySet()) {
      intsToStationsNames.put(stationsNamesToInts.get(station), station);
    }
    return intsToStationsNames;
  }

  private Map<String, Integer> getStationsNamesToInts() throws IOException {
    Map<String, Integer> stationNamesToInts = new LinkedHashMap<>();
    List<String> uniqueStationsNames = FileUtil.getLines("all_stations.txt");
    for (int i = 0; i < uniqueStationsNames.size(); i++) {
      stationNamesToInts.put(uniqueStationsNames.get(i), i);
    }
    return stationNamesToInts;
  }

  private List<List<int[]>> getGraph() {
    int numberOfVertices = 68;
    List<List<int[]>> graph = new ArrayList<>(numberOfVertices);
    Set<Integer> sortedStations1 =
        new HashSet<>(mapSrcToMapDestinationToDistanceInM.keySet());

    for (int station1 : sortedStations1) {
      Map<Integer, Integer> station1Map = mapSrcToMapDestinationToDistanceInM.get(station1);
      List<int[]> stations2 = new ArrayList<>();

      for (int station2 : station1Map.keySet()) {
        stations2.add(new int[] {station2, station1Map.get(station2)});
      }

      graph.add(stations2);
    }

    return graph;
  }
}
