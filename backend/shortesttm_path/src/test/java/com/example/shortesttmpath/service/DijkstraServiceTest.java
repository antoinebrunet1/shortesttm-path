package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraServiceTest {
  /*
  Graph:

  0-1-1
  \   |
   4  2
    \ |
      2
   */
  @Test
  public void dijkstraReturnsCorrectPathThreeNodes() {
    List<Edge> node1 = List.of(
        new Edge(1, 1),
        new Edge(2, 4)
    );
    List<Edge> node2 = List.of(
        new Edge(0, 1),
        new Edge(2, 2)
    );
    List<Edge> node3 = List.of(
        new Edge(1, 2),
        new Edge(0, 4)
    );
    List<List<Edge>> graph = List.of(
        node1,
        node2,
        node3
    );
    int start = 0;
    int target = 2;
    List<Integer> expectedResult = List.of(0, 1, 2);
    List<Integer> actualResult = new DijkstraService().dijkstra(graph, start, target);

    Assertions.assertEquals(expectedResult, actualResult);
  }
}
