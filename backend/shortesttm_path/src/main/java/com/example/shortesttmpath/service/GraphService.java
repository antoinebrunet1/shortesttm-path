package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * To get the graph used for the Dijkstra algorithm.
 */
@Service
public class GraphService {
  private final Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM;
  private final int numberOfStations;

  /**
   * The constructor.
   *
   * @param stationRepository The StationRepository.
   * @param distancesService The DistancesService.
   * @throws IOException IOException.
   */
  public GraphService(StationRepository stationRepository, DistancesService distancesService)
      throws IOException {
    mapSrcToMapDestinationToDistanceInM =
        distancesService.getMapScrToMapDestinationToDistanceInM(stationRepository
            .getStationsNamesToInts());
    numberOfStations = stationRepository.getStationsNamesToInts().size();
  }

  /**
   * Returns the graph used for the Dijkstra algorithm.
   *
   * @return The graph used for the Dijkstra algorithm.
   */
  public List<List<Edge>> getGraph() {
    List<List<Edge>> graph = new ArrayList<>(numberOfStations);

    for (int i = 0; i < numberOfStations; i++) {
      addSrcStationToGraph(graph, i);
    }

    return graph;
  }

  private void addSrcStationToGraph(List<List<Edge>> graph, int srcStation) {
    Map<Integer, Integer> mapDestinationToDistanceInM =
        mapSrcToMapDestinationToDistanceInM.get(srcStation);
    List<Edge> destinationAndDistanceInM = new ArrayList<>();

    for (int destinationStation : mapDestinationToDistanceInM.keySet()) {
      destinationAndDistanceInM.add(new Edge(destinationStation,
          mapDestinationToDistanceInM.get(destinationStation)));
    }

    graph.add(destinationAndDistanceInM);
  }
}
