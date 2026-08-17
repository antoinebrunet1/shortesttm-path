package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphService {
  private final Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM;
  private final int numberOfStations;

  public GraphService(StationRepository stationRepository) throws IOException {
    mapSrcToMapDestinationToDistanceInM =
        DistancesService.getMapScrToMapDestinationToDistanceInM(stationRepository
            .getStationsNamesToInts());
    numberOfStations = stationRepository.getStationsNamesToInts().size();
  }

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
