package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

/**
 * To get the graph used for the Dijkstra algorithm.
 */
@Service
public class GraphService {
  private final StationRepository stationRepository;
  private final FileService fileService;

  /**
   * The constructor.
   *
   * @param stationRepository The StationRepository.
   * @param fileService The FileService.
   */
  public GraphService(StationRepository stationRepository, FileService fileService) {
    this.stationRepository = stationRepository;
    this.fileService = fileService;
  }

  /**
   * Returns the graph used for the Dijkstra algorithm.
   *
   * @return The graph used for the Dijkstra algorithm.
   * @throws IOException IOException.
   */
  public List<List<Edge>> getGraph() throws IOException {
    return new ArrayList<>(getEdges().values());
  }

  private Map<Integer, List<Edge>> getEdges() throws IOException {
    Map<Integer, List<Edge>> edges = new LinkedHashMap<>();
    List<String> distancesLines = fileService.getLines("distances.txt");

    for (String distanceLine : distancesLines) {
      addDistanceLine(edges, distanceLine);
    }

    // Sorts the keys of edges.
    edges = new TreeMap<>(edges);

    // Sorts the lists of edges.
    edges.values().forEach(list -> list.sort(Comparator.comparing(Edge::destination)));

    return edges;
  }

  private void addDistanceLine(Map<Integer, List<Edge>> edges, String distanceLine) {
    int src = stationRepository.getStationsNamesToInts().get(distanceLine.split(" to ")[0]);
    int destination = stationRepository.getStationsNamesToInts()
        .get(distanceLine.split(" to ")[1].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[1].split("\\s:\\s")[1]);

    addEdge(edges, src, destination, distance);
    addEdge(edges, destination, src, distance);
  }

  private void addEdge(Map<Integer, List<Edge>> edges, int src, int destination, int distance) {
    if (edges.containsKey(src)) {
      edges.get(src).add(new Edge(destination, distance));
    } else {
      List<Edge> list = new ArrayList<>();

      list.add(new Edge(destination, distance));
      edges.put(src, list);
    }
  }
}
