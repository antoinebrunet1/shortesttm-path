package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShortestPathServiceTest {
  @Mock
  StationRepository stationRepository = mock(StationRepository.class);
  @Mock
  DijkstraService dijkstraService = mock(DijkstraService.class);
  @Mock
  GraphService graphService = mock(GraphService.class);
  @InjectMocks
  ShortestPathService shortestPathService;

  private void mockInjections() {
    mockStationRepository();
    mockDijkstraService();
    mockGraphService();
  }

  private void mockStationRepository() {
    Map<Line, List<String>> linesToStations = Map.of(
        Line.BLUE, List.of("A", "B", "C"),
        Line.GREEN, List.of("B", "D"),
        Line.ORANGE, List.of("C", "D"),
        Line.YELLOW, List.of("C", "E")
    );
    List<String> allStationsAlphabeticalOrder = List.of("A", "B", "C", "D", "E");
    List<String> allStationsToSwitchLines = List.of("B", "C");
    Map<String, Integer> stationsNamesToInts = Map.of(
        "A", 0,
        "B", 1,
        "C", 2,
        "D", 3,
        "E", 4
    );
    Map<Integer, String> intsToStationsNames = Map.of(
        0, "A",
        1, "B",
        2, "C",
        3, "D",
        4, "E"
    );
    when(stationRepository.getLinesToStations()).thenReturn(linesToStations);
    when(stationRepository.getAllStationsAlphabeticalOrder()).thenReturn(allStationsAlphabeticalOrder);
    when(stationRepository.getAllStationsToSwitchLines()).thenReturn(allStationsToSwitchLines);
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);
    when(stationRepository.getIntsToStationsNames()).thenReturn(intsToStationsNames);
  }

  private void mockDijkstraService() {
    List<Integer> path = List.of(0, 1, 3);
    when(dijkstraService.dijkstra(any(), any(), any())).thenReturn(path);
  }

  private void mockGraphService() {
    List<List<Edge>> graph = List.of(
        List.of(
            new Edge(1, 100)
        ),
        List.of(
            new Edge(0, 100),
            new Edge(2, 300),
            new Edge(3, 200)
        ),
        List.of(
            new Edge(1, 300),
            new Edge(3, 400),
            new Edge(4, 500)
        ),
        List.of(
            new Edge(1, 200),
            new Edge(2, 400)
        ),
        List.of(
            new Edge(2, 500)
        )
    );
    when(graphService.getGraph()).thenReturn(graph);
  }

  /*
  Graph (Each vertex is bidirectional.):

              D       E
             / \     /
           200 400 500
           /     \ /
  A--100--B--300--C

  Same graph but with A = 0, B = 1, ... E = 4:

              3       4
             / \     /
           200 400 500
           /     \ /
  0--100--1--300--2
   */
    @Test
    public void getShortestPathHappyPath() throws IOException {
      mockInjections();

      String startingStation = "A";
      String destinationStation = "D";
      ShortestPathBean actualResult = shortestPathService.getShortestPath(startingStation, destinationStation);
      ShortestPathBean expectedResult = new ShortestPathBean(
          new NonEndingStationInPathBean(
              startingStation,
              Line.BLUE,
              "C"
          ),
          destinationStation,
          List.of(new NonEndingStationInPathBean(
              "B",
              Line.GREEN,
              "D"
          )));

      assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        assertThrows(StationsOnSameLineException.class, () ->
            shortestPathService.getShortestPath("McGill", "Viau"));
    }

  @Test
  public void getShortestPathInvalidStartingStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("MMcGill", "Viau"));
  }

  @Test
  public void getShortestPathInvalidDestinationStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("McGill", "VViau"));
  }

  @Test
  public void getShortestPathInvalidStationsShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("MMcGill", "VViau"));
  }
}
