package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.repository.StationRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
@ExtendWith(MockitoExtension.class)
public class ShortestPathServiceTest {
  @Mock
  StationRepository stationRepository;
  @Mock
  DijkstraService dijkstraService;
  @Mock
  GraphService graphService;
  @InjectMocks
  ShortestPathService shortestPathService;
  private final Map<Line, List<String>> linesToStations = Map.of(
      Line.BLUE, List.of("A", "B", "C"),
      Line.GREEN, List.of("B", "D"),
      Line.ORANGE, List.of("C", "D"),
      Line.YELLOW, List.of("C", "E")
  );
  private final Map<String, Integer> stationsNamesToInts = Map.of(
      "A", 0,
      "B", 1,
      "C", 2,
      "D", 3,
      "E", 4
  );

  private void mockInjectionsHappyPath() {
    mockStationRepository();
    mockDijkstraService();
  }

  private void mockStationRepository() {
    List<String> allStationsToSwitchLines = List.of("B", "C");
    Map<Integer, String> intsToStationsNames = Map.of(
        0, "A",
        1, "B",
        2, "C",
        3, "D",
        4, "E"
    );
    when(stationRepository.getLinesToStations()).thenReturn(linesToStations);
    when(stationRepository.getAllStationsToSwitchLines()).thenReturn(allStationsToSwitchLines);
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);
    when(stationRepository.getIntsToStationsNames()).thenReturn(intsToStationsNames);
  }

  private void mockDijkstraService() {
    List<Integer> path = List.of(0, 1, 3);
    when(dijkstraService.dijkstra(any(), anyInt(), anyInt())).thenReturn(path);
  }

    @Test
    public void getShortestPathHappyPath() {
      mockInjectionsHappyPath();

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
      when(stationRepository.getLinesToStations()).thenReturn(linesToStations);
      when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);

      assertThrows(StationsOnSameLineException.class, () ->
            shortestPathService.getShortestPath("A", "B"));
    }

  @Test
  public void getShortestPathInvalidStartingStationShouldThrowStationsNotValidException() {
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);

    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("AA", "D"));
  }

  @Test
  public void getShortestPathInvalidDestinationStationShouldThrowStationsNotValidException() {
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);

    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("A", "DD"));
  }

  @Test
  public void getShortestPathInvalidStationsShouldThrowStationsNotValidException() {
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);

    assertThrows(StationsNotValidException.class, () ->
        shortestPathService.getShortestPath("AA", "DD"));
  }
}
