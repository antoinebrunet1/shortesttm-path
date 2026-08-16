package com.example.shortesttmpath.util;

import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphUtil {
  private final Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM;
  private final int numberOfStations;

  public GraphUtil(StationRepository stationRepository) throws IOException {
    mapSrcToMapDestinationToDistanceInM =
        DistancesUtil.getMapScrToMapDestinationToDistanceInM(stationRepository
            .getStationsNamesToInts());
    numberOfStations = stationRepository.getStationsNamesToInts().size();
  }

  public List<List<int[]>> getGraph() {
    List<List<int[]>> graph = new ArrayList<>(numberOfStations);

    for (int i = 0; i < numberOfStations; i++) {
      addSrcStationToGraph(graph, i);
    }

    return graph;
  }

  private void addSrcStationToGraph(List<List<int[]>> graph, int srcStation) {
    Map<Integer, Integer> mapDestinationToDistanceInM =
        mapSrcToMapDestinationToDistanceInM.get(srcStation);
    List<int[]> destinationAndDistanceInM = new ArrayList<>();

    for (int destinationStation : mapDestinationToDistanceInM.keySet()) {
      destinationAndDistanceInM.add(new int[] {destinationStation,
          mapDestinationToDistanceInM.get(destinationStation)});
    }

    graph.add(destinationAndDistanceInM);
  }
}
