package com.example.shortesttmpath.service;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
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
  @InjectMocks
  ShortestPathService shortestPathService;
  private final Map<Line, List<Station>> linesToStations = Map.of(
      Line.BLUE, List.of(Station.ACADIE, Station.BEAUBIEN, Station.CADILLAC),
      Line.GREEN, List.of(Station.BEAUBIEN, Station.DE_CASTELNAU),
      Line.ORANGE, List.of(Station.CADILLAC, Station.DE_CASTELNAU),
      Line.YELLOW, List.of(Station.CADILLAC, Station.EDOUARD_MONTPETIT)
  );

  private void mockInjectionsHappyPath() {
    mockStationRepository();
    mockDijkstraService();
  }

  private void mockStationRepository() {
    List<Station> allStationsToSwitchLines = List.of(Station.BEAUBIEN, Station.CADILLAC);
    when(stationRepository.getLinesToStations()).thenReturn(linesToStations);
    when(stationRepository.getAllStationsToSwitchLines()).thenReturn(allStationsToSwitchLines);
  }

  private void mockDijkstraService() {
    List<Integer> path = List.of(0, 1, 3);
    when(dijkstraService.dijkstra(any(), anyInt(), anyInt())).thenReturn(path);
  }

    @Test
    public void getShortestPathHappyPath() {
      mockInjectionsHappyPath();

      Station startingStation = Station.ACADIE;
      Station destinationStation = Station.DE_CASTELNAU;
      ShortestPathBean actualResult = shortestPathService.getShortestPath(startingStation, destinationStation);
      ShortestPathBean expectedResult = new ShortestPathBean(
          new NonEndingStationInPathBean(
              startingStation,
              Line.BLUE,
              Station.CADILLAC
          ),
          destinationStation,
          List.of(new NonEndingStationInPathBean(
              Station.BEAUBIEN,
              Line.GREEN,
              Station.DE_CASTELNAU
          )));

      assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
      when(stationRepository.getLinesToStations()).thenReturn(linesToStations);

      assertThrows(StationsOnSameLineException.class, () ->
            shortestPathService.getShortestPath(Station.ACADIE, Station.BEAUBIEN));
    }
}
