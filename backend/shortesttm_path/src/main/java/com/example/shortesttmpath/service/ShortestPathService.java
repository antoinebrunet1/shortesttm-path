package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.repository.GraphRepository;
import com.example.shortesttmpath.repository.StationRepository;
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
   * @param graphRepository The GraphRepository.
   */
  public ShortestPathService(StationRepository stationRepository, DijkstraService dijkstraService,
                             GraphRepository graphRepository) {
    this.stationRepository = stationRepository;
    this.dijkstraService = dijkstraService;
    graph = graphRepository.getEdges();
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
    if (!Collections.disjoint(getLines(startingStation), getLines(destinationStation))) {
      throw new StationsOnSameLineException();
    }

    List<Station> pathStations =
        dijkstraService.dijkstra(graph, startingStation.ordinal(), destinationStation.ordinal())
        .stream()
        .map(index -> Station.values()[index])
        .toList();

    return getShortestPathBean(startingStation, destinationStation, pathStations);
  }

  private Station getDirectionOfStation(Station station, Station nextStation) {
    Line line = getLines(station).stream().filter(getLines(nextStation)::contains).findFirst()
        .orElseThrow();
    List<Station> stations = stationRepository.getLinesToStations().get(line);
    boolean nextStationIsAfter = stations.get(stations.indexOf(station) + 1).equals(nextStation);

    return nextStationIsAfter ? stations.getLast() : stations.getFirst();
  }

  private ShortestPathBean getShortestPathBean(Station startingStation,
                                                      Station destinationStation,
                                                      List<Station> pathStations) {
    List<Station> stationsToSwitchLines = new ArrayList<>(getStationsToSwitchLines(pathStations,
        startingStation, destinationStation));
    stationsToSwitchLines.removeAll(getFalseTransfers(stationsToSwitchLines, pathStations));

    return new ShortestPathBean(
        getStationObject(startingStation, pathStations),
        destinationStation,
        new ArrayList<>(getStationsToSwitchLinesObjects(stationsToSwitchLines, pathStations))
    );
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

  private List<Station> getFalseTransfers(List<Station> stationsToSwitchLines,
                                         List<Station> pathStations) {
    return stationsToSwitchLines
        .stream()
        .filter(station -> {
          int stationIndex = pathStations.indexOf(station);
          Station stationBefore = pathStations.get(stationIndex - 1);
          Station stationAfter = pathStations.get(stationIndex + 1);

          return getLines(stationBefore).getFirst().equals(getLines(stationAfter).getFirst())
              && getLines(station).contains(getLines(stationBefore).getFirst());
        })
        .toList();
  }

  private List<Line> getLines(Station station) {
    return stationRepository.getLinesToStations().keySet()
        .stream()
        .filter(line -> stationRepository.getLinesToStations().get(line).contains(station))
        .toList();
  }
}
