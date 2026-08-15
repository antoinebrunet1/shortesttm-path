package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.repository.StationRepository;
import com.example.shortesttmpath.util.DijkstraUtil;
import com.example.shortesttmpath.util.GraphUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service to calculate the shortest metro path between two STM metro stations.
 */
@Service
public class ShortestPathService {
  private final StationRepository stationRepository = new StationRepository();
  private final List<List<int[]>> graph = new GraphUtil(stationRepository)
      .getGraph(stationRepository.getStationsNamesToInts());

  public ShortestPathService() throws IOException {
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public List<String> getAllStationsInAlphabeticalOrder() {
    return stationRepository.getAllStationsAlphabeticalOrder();
  }

  private String getDirectionOfStation(String station1, String station2) {
    List<String> station1Lines = getLines(station1);
    List<String> station2Lines = getLines(station2);
    String lineOfDirection = getLineOfDirection(station1Lines, station2Lines);
    List<String> allStationsOfLineOfDirection = getAllStationsOfLineOfDirection(lineOfDirection);
    int indexOfStation1OnLine = allStationsOfLineOfDirection.indexOf(station1);
    int indexOfStation2OnLine = allStationsOfLineOfDirection.indexOf(station2);
    List<String> stationsOfLineOfDirection = stationRepository.getLinesToStations()
        .get(Line.valueOf(lineOfDirection));
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
    return stationRepository.getLinesToStations().get(Line.valueOf(lineOfDirection));
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
    int start = stationRepository.getStationsNamesToInts().get(startingStation);
    int target = stationRepository.getStationsNamesToInts().get(destinationStation);
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
        .map(stationRepository.getIntsToStationsNames()::get)
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
    return allStations.stream().filter(station -> stationRepository.getAllStationsToSwitchLines()
        .contains(station) && !List.of(startingStation, destinationStation).contains(station))
        .toList();
  }

  private boolean areStationsOnTheSameLine(String startingStation,
                                                  String destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private boolean areInputStationsValid(String startingStation, String destinationStation) {
    return stationRepository.getStationsNamesToInts().containsKey(startingStation)
        && stationRepository.getStationsNamesToInts().containsKey(destinationStation);
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

    for (Line line : stationRepository.getLinesToStations().keySet()) {
      if (stationRepository.getLinesToStations().get(line).contains(station)) {
        lines.add(line.name());
      }
    }

    return lines;
  }
}
