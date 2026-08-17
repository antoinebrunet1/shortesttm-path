package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.springframework.stereotype.Service;

/**
 * Contains Dijkstra's algorithm to find the shortest path.
 * Source: <a href="https://medium.com/@robinviktorsson/dijkstras-algorithm-in-java-learn-with-practical-examples-9e7af310e466">...</a>.
 */
@Service
public class DijkstraService {
  /**
   * The default constructor.
   */
  public DijkstraService() {
  }

  /**
   * Dijkstra's algorithm to find the shortest path
   * Source: <a href="https://medium.com/@robinviktorsson/dijkstras-algorithm-in-java-learn-with-practical-examples-9e7af310e466">...</a>.
   * The values of the nodes are 0, 1, 2...
   *
   * @param graph The graph. The main List is for every node. For every node, there is a List of
   *              (to, weight) pairs.
   * @param start The value of the start node.
   * @param target The value of the target node.
   * @return The nodes of the path as ints.
   */
  public static List<Integer> dijkstra(List<List<Edge>> graph, int start, int target) {

    // Number of nodes in the graph
    int n = graph.size();

    // Stores shortest known distance from start node
    int[] dist = new int[n];

    // Stores previous node for path reconstruction
    int[] parent = new int[n];

    // Initialize all distances to infinity
    Arrays.fill(dist, Integer.MAX_VALUE);

    // Initialize parents as undefined
    Arrays.fill(parent, -1);

    // Distance to start node is 0
    dist[start] = 0;

    // Min-heap priority queue:
    // each element = {distance, node}
    PriorityQueue<int[]> pq =
        new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    // Start with the source node
    pq.offer(new int[]{0, start});

    // Process nodes until queue is empty
    while (!pq.isEmpty()) {

      // Get node with smallest distance
      int[] curr = pq.poll();

      int d = curr[0];
      int node = curr[1];

      // Skip outdated queue entries
      if (d > dist[node]) {
        continue;
      }

      // Explore all neighbors of current node
      for (Edge edge : graph.get(node)) {

        int neighbor = edge.destination();

        // Calculate new possible distance
        int newDist = dist[node] + edge.distance();

        // If a shorter path is found
        if (newDist < dist[neighbor]) {

          // Update shortest distance
          dist[neighbor] = newDist;

          // Remember best previous node
          parent[neighbor] = node;

          // Add updated distance to priority queue
          pq.offer(new int[]{newDist, neighbor});
        }
      }
    }

    // Reconstruct shortest path
    List<Integer> path = new ArrayList<>();

    // Backtrack from target using parent array
    for (int at = target; at != -1; at = parent[at]) {
      path.add(at);
    }

    // Reverse because path was built backwards
    Collections.reverse(path);

    return path;
  }
}
