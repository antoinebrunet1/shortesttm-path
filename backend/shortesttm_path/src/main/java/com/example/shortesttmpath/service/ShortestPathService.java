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
import org.springframework.stereotype.Service;

/**
 * Service to calculate the shortest metro path between two STM metro stations.
 */
@Service
public class ShortestPathService {
  private final StationRepository stationRepository;
  private final DijkstraService dijkstraService;
  private final List<List<Edge>> graph;

  /**
   * The constructor.
   *
   * @param stationRepository The StationRepository.
   * @param dijkstraService The DijkstraService.
   * @param graphService The GraphService.
   * @throws IOException IOException.
   */
  public ShortestPathService(StationRepository stationRepository, DijkstraService dijkstraService,
                             GraphService graphService) throws IOException {
    this.stationRepository = stationRepository;
    this.dijkstraService = dijkstraService;
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
    List<String> pathStations = getPathStations(start, target);

    return getShortestPathBean(startingStation, destinationStation, pathStations);
  }

  private String getDirectionOfStation(String station, String nextStation) {
    Line line = getLineOfDirection(getLines(station), getLines(nextStation));
    List<String> stations = stationRepository.getLinesToStations().get(line);
    boolean nextStationIsAfter = stations.get(stations.indexOf(station) + 1).equals(nextStation);

    return nextStationIsAfter ? stations.getLast() : stations.getFirst();
  }

  private Line getLineOfDirection(List<Line> stationLines, List<Line> nextStationLines) {
    return stationLines.stream().filter(nextStationLines::contains).findFirst().orElseThrow();
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
    return dijkstraService.dijkstra(graph, start, target)
        .stream()
        .map(stationRepository.getIntsToStationsNames()::get)
        .toList();
  }

  private ShortestPathBean getShortestPathBean(String startingStation,
                                                      String destinationStation,
                                                      List<String> pathStations) {
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(getStationObject(startingStation, pathStations));
    shortestPath.setDestinationStation(destinationStation);
    List<String> stationsToSwitchLines = new ArrayList<>(getStationsToSwitchLines(pathStations,
        startingStation, destinationStation));
    stationsToSwitchLines.removeAll(filterOutFalseTransfers(stationsToSwitchLines, pathStations));
    shortestPath.setStationsToSwitchLines(getStationsToSwitchLinesObjects(
        stationsToSwitchLines, pathStations));

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
                                                             List<String> pathStations) {
    String nextStation = pathStations.get(pathStations.indexOf(station) + 1);
    String direction = getDirectionOfStation(station, nextStation);
    Line line = getLineOfStationInGivenDirection(station, direction);

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private Line getLineOfStationInGivenDirection(String station, String direction) {
    List<Line> lines = getLines(direction);

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

  private boolean isStationFalseTransfer(String station, String stationBefore,
                                         String stationAfter) {
    return getLines(stationBefore).getFirst().equals(getLines(stationAfter).getFirst())
        && getLines(station).contains(getLines(stationBefore).getFirst());
  }

  private List<Line> getLines(String station) {
    List<Line> lines = new ArrayList<>();

    for (Line line : stationRepository.getLinesToStations().keySet()) {
      if (stationRepository.getLinesToStations().get(line).contains(station)) {
        lines.add(line);
      }
    }

    return lines;
  }
}
