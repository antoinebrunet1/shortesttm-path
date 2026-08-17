package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to calculate the shortest metro path between two STM metro stations.
 */
@Service
public class ShortestPathService {
  private final StationRepository stationRepository;
  private final List<List<Edge>> graph;

  public ShortestPathService(StationRepository stationRepository, GraphService graphService) {
    this.stationRepository = stationRepository;
    graph = graphService.getGraph();
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

  private String getDirectionOfStation(String station, String nextStation) {
    Line lineOfDirection = getLineOfDirection(stationRepository.getLines(station), stationRepository.getLines(nextStation));
    List<String> allStationsOfLineOfDirection = getAllStationsOfLineOfDirection(lineOfDirection);
    int indexOfStation1OnLine = allStationsOfLineOfDirection.indexOf(station);
    int indexOfStation2OnLine = allStationsOfLineOfDirection.indexOf(nextStation);
    List<String> stationsOfLineOfDirection = stationRepository.getLinesToStations()
        .get(lineOfDirection);
    List<String> directions = List.of(stationsOfLineOfDirection.getFirst(),
        stationsOfLineOfDirection.getLast());

    return indexOfStation1OnLine < indexOfStation2OnLine ? directions.getLast() :
        directions.getFirst();
  }

  private Line getLineOfDirection(List<Line> stationLines, List<Line> nextStationLines) {
    return stationLines.stream()
        .distinct()
        .filter(nextStationLines::contains)
        .collect(Collectors.toSet()).iterator().next();
  }

  private List<String> getAllStationsOfLineOfDirection(Line lineOfDirection) {
    return stationRepository.getLinesToStations().get(lineOfDirection);
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
    return DijkstraService.dijkstra(graph, start, target)
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
    List<String> stationsToExclude = filterOutFalseTransfers(stationsToSwitchLines, allStations);
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
    Line line = getLineOfStationInGivenDirection(station, direction);

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private Line getLineOfStationInGivenDirection(String station, String direction) {
    List<Line> lines = stationRepository.getLines(direction);

    if (lines.size() == 1) {
      return lines.getFirst();
    }

    return lines
        .stream()
        .filter(line -> stationRepository.getLines(station).contains(line)
            && stationRepository.getLines(direction).contains(line))
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
    return !Collections.disjoint(stationRepository.getLines(startingStation), stationRepository.getLines(destinationStation));
  }

  private boolean areInputStationsValid(String startingStation, String destinationStation) {
    return stationRepository.getStationsNamesToInts().containsKey(startingStation)
        && stationRepository.getStationsNamesToInts().containsKey(destinationStation);
  }

  private List<String> filterOutFalseTransfers(List<String> stationsToSwitchLines,
                                               List<String> allStations) {
    List<String> stationsToExclude = new ArrayList<>();
    for (String station : stationsToSwitchLines) {
      int indexOfStationInAllStations = allStations.indexOf(station);
      String stationBefore = allStations.get(indexOfStationInAllStations - 1);
      String stationAfter = allStations.get(indexOfStationInAllStations + 1);
      if (isStationFalseTransfer(station, stationBefore, stationAfter)) {
        stationsToExclude.add(station);
      }
    }
    return stationsToExclude;
  }

  private boolean isStationFalseTransfer(String station, String stationBefore, String stationAfter) {
    return stationRepository.getLines(stationBefore).getFirst().equals(stationRepository.getLines(stationAfter).getFirst())
        && stationRepository.getLines(station).contains(stationRepository.getLines(stationBefore).getFirst());
  }
}
