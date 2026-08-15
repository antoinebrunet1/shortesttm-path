package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.exception.StationsNotValidException;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.service.ShortestPathService;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortestPathUtilTest {
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
      String startingStation = "A";
      String destinationStation = "D";
      List<String> blueLineStations = List.of("A", "B", "C");
      List<String> greenLineStations = List.of("B", "D");
      List<String> orangeLineStations = List.of("C", "D");
      List<String> yellowLineStations = List.of("C", "E");
      List<String> allStationsAlphabeticalOrder = List.of("A", "B", "C", "D", "E");
      List<String> allStationsToSwitchLines = List.of("B", "C");
      Map<String, Integer> stationsNamesToInts = Map.of(
          "A", 0,
          "B", 1,
          "C", 2,
          "D", 3,
          "E", 4
      );
      Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM = Map.of(
          0, Map.of(
              1, 100
          ),
          1, Map.of(
              0, 100,
              3, 200,
              2, 300
          ),
          2, Map.of(
              1, 300,
              3, 400,
              4, 500
          ),
          3, Map.of(
              1, 200,
              2, 400
          ),
          4, Map.of(
              2, 500
          )
      );
      List<Integer> path = List.of(0, 1, 3);

        try (MockedStatic<FileUtil> fileUtilMocked = Mockito.mockStatic(FileUtil.class);
             MockedStatic<DistancesUtil> distancesUtilMocked = Mockito.mockStatic(DistancesUtil.class);
             MockedStatic<DijkstraUtil> dijkstraUtilMocked = Mockito.mockStatic(DijkstraUtil.class)) {
          fileUtilMocked.when(() -> FileUtil.getLines("blue_line_stations.txt")).thenReturn(blueLineStations);
          fileUtilMocked.when(() -> FileUtil.getLines("green_line_stations.txt")).thenReturn(greenLineStations);
          fileUtilMocked.when(() -> FileUtil.getLines("orange_line_stations.txt")).thenReturn(orangeLineStations);
          fileUtilMocked.when(() -> FileUtil.getLines("yellow_line_stations.txt")).thenReturn(yellowLineStations);
          fileUtilMocked.when(() -> FileUtil.getLines("all_stations_alphabetical_order.txt")).thenReturn(allStationsAlphabeticalOrder);
          fileUtilMocked.when(() -> FileUtil.getLines("all_stations_to_switch_lines.txt")).thenReturn(allStationsToSwitchLines);
          distancesUtilMocked.when(() -> DistancesUtil.getMapScrToMapDestinationToDistanceInM(stationsNamesToInts)).thenReturn(mapSrcToMapDestinationToDistanceInM);
          dijkstraUtilMocked.when(() -> DijkstraUtil.dijkstra(Mockito.any(), Mockito.eq(0), Mockito.eq(3))).thenReturn(path);
          ShortestPathBean actualPath = new ShortestPathService().getShortestPath(
              startingStation, destinationStation);
          ShortestPathBean expectedPath = new ShortestPathBean(
              new NonEndingStationInPathBean(
                  startingStation,
                  "BLUE",
                  "C"
              ),
              destinationStation,
              List.of(new NonEndingStationInPathBean(
                  "B",
                  "GREEN",
                  "D"
              )));

          assertEquals(expectedPath, actualPath);
        }
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        assertThrows(StationsOnSameLineException.class, () ->
            new ShortestPathService().getShortestPath("McGill", "Viau"));
    }

  @Test
  public void getShortestPathInvalidStartingStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        new ShortestPathService().getShortestPath("MMcGill", "Viau"));
  }

  @Test
  public void getShortestPathInvalidDestinationStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        new ShortestPathService().getShortestPath("McGill", "VViau"));
  }

  @Test
  public void getShortestPathInvalidStationsShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        new ShortestPathService().getShortestPath("MMcGill", "VViau"));
  }
}
