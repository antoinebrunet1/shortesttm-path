package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
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
  public ShortestPathBean getShortestPath(Station startingStation,
                                          Station destinationStation) {
    validateStations(startingStation, destinationStation);
    List<Station> pathStations = getPathStations(startingStation.ordinal(),
        destinationStation.ordinal());

    return getShortestPathBean(startingStation, destinationStation, pathStations);
  }

  private Station getDirectionOfStation(Station station, Station nextStation) {
    Line line = getLineOfDirection(getLines(station), getLines(nextStation));
    List<Station> stations = stationRepository.getLinesToStations().get(line);
    boolean nextStationIsAfter = stations.get(stations.indexOf(station) + 1).equals(nextStation);

    return nextStationIsAfter ? stations.getLast() : stations.getFirst();
  }

  private Line getLineOfDirection(List<Line> stationLines, List<Line> nextStationLines) {
    return stationLines.stream().filter(nextStationLines::contains).findFirst().orElseThrow();
  }

  private void validateStations(Station startingStation, Station destinationStation) {
    if (areStationsOnTheSameLine(startingStation, destinationStation)) {
      throw new StationsOnSameLineException();
    }
  }

  private List<Station> getPathStations(int start, int target) {
    return dijkstraService.dijkstra(graph, start, target)
        .stream()
        .map(index -> Station.values()[index])
        .toList();
  }

  private ShortestPathBean getShortestPathBean(Station startingStation,
                                                      Station destinationStation,
                                                      List<Station> pathStations) {
    ShortestPathBean shortestPath = new ShortestPathBean();
    shortestPath.setStartingStation(getStationObject(startingStation, pathStations));
    shortestPath.setDestinationStation(destinationStation);
    List<Station> stationsToSwitchLines = new ArrayList<>(getStationsToSwitchLines(pathStations,
        startingStation, destinationStation));
    stationsToSwitchLines.removeAll(getFalseTransfers(stationsToSwitchLines, pathStations));
    shortestPath.setStationsToSwitchLines(new ArrayList<>(getStationsToSwitchLinesObjects(
        stationsToSwitchLines, pathStations)));

    return shortestPath;
  }

  private List<NonEndingStationInPathBean> getStationsToSwitchLinesObjects(
      List<Station> stationsToSwitchLines, List<Station> pathStations) {
    return stationsToSwitchLines
        .stream()
        .map(station -> getStationObject(station, pathStations))
        .toList();
  }

  private NonEndingStationInPathBean getStationObject(Station station,
                                                             List<Station> pathStations) {
    Station nextStation = pathStations.get(pathStations.indexOf(station) + 1);
    Station direction = getDirectionOfStation(station, nextStation);
    Line line = getLineOfStationInGivenDirection(station, direction);

    return new NonEndingStationInPathBean(station, line, direction);
  }

  private Line getLineOfStationInGivenDirection(Station station, Station direction) {
    return getLines(direction)
        .stream()
        .filter(line -> getLines(station).contains(line)
            && getLines(direction).contains(line))
        .toList()
        .getFirst();
  }

  private List<Station> getStationsToSwitchLines(List<Station> pathStations,
                                                       Station startingStation,
                                                       Station destinationStation) {
    return pathStations.stream().filter(station -> stationRepository.getAllStationsToSwitchLines()
        .contains(station) && !List.of(startingStation, destinationStation).contains(station))
        .toList();
  }

  private boolean areStationsOnTheSameLine(Station startingStation,
                                                  Station destinationStation) {
    return !Collections.disjoint(getLines(startingStation), getLines(destinationStation));
  }

  private List<Station> getFalseTransfers(List<Station> stationsToSwitchLines,
                                         List<Station> pathStations) {
    return stationsToSwitchLines
        .stream()
        .filter(station -> isStationFalseTransfer(station,
            pathStations.get(pathStations.indexOf(station) - 1),
            pathStations.get(pathStations.indexOf(station) + 1)))
        .toList();
  }

  private boolean isStationFalseTransfer(Station station, Station stationBefore,
                                         Station stationAfter) {
    return getLines(stationBefore).getFirst().equals(getLines(stationAfter).getFirst())
        && getLines(station).contains(getLines(stationBefore).getFirst());
  }

  private List<Line> getLines(Station station) {
    return stationRepository.getLinesToStations().keySet()
        .stream()
        .filter(line -> stationRepository.getLinesToStations().get(line).contains(station))
        .toList();
  }
}
