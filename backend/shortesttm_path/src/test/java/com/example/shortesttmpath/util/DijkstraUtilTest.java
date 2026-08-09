package com.example.shortesttmpath.util;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraUtilTest {
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
    List<int[]> node1 = List.of(
        new int[] {1, 1},
        new int[] {2, 4}
    );
    List<int[]> node2 = List.of(
        new int[] {0, 1},
        new int[] {2, 2}
    );
    List<int[]> node3 = List.of(
        new int[] {1, 2},
        new int[] {0, 4}
    );
    List<List<int[]>> graph = List.of(
        node1,
        node2,
        node3
    );
    int start = 0;
    int target = 2;
    List<Integer> expectedResult = List.of(0, 1, 2);
    List<Integer> actualResult = DijkstraUtil.dijkstra(graph, start, target);

    Assertions.assertEquals(expectedResult, actualResult);
  }
}
