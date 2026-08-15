package com.example.shortesttmpath.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphUtil {
  public static List<List<int[]>> getGraph(Map<String, Integer> stationsNamesToInts,
                                           Map<Integer, Map<Integer, Integer>>
                                               mapSrcToMapDestinationToDistanceInM) {
    List<List<int[]>> graph = new ArrayList<>(stationsNamesToInts.size());

    for (int srcStation : stationsNamesToInts.values()) {
      addSrcStationToGraph(graph, srcStation, mapSrcToMapDestinationToDistanceInM);
    }

    return graph;
  }

  private static void addSrcStationToGraph(List<List<int[]>> graph, int srcStation, Map<Integer, Map<Integer, Integer>>
      mapSrcToMapDestinationToDistanceInM) {
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
