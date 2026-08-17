package com.example.shortesttmpath.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.shortesttmpath.data.Edge;
import com.example.shortesttmpath.repository.StationRepository;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GraphServiceTest {
  @Mock
  StationRepository stationRepository;
  @Mock
  DistancesService distancesService;

  private void mockInjections() throws IOException {
    mockStationRepository();
    mockDistancesService();
  }

  private void mockStationRepository() {
    Map<String, Integer> stationsNamesToInts = Map.of(
        "Angrignon", 0,
        "Jolicoeur", 1,
        "Monk", 2
    );
    when(stationRepository.getStationsNamesToInts()).thenReturn(stationsNamesToInts);
  }

  private void mockDistancesService() throws IOException {
    Map<Integer, Integer> mapDestinationToDistanceInMForSource0 = new LinkedHashMap<>();
    mapDestinationToDistanceInMForSource0.put(2, 844);
    Map<Integer, Integer> mapDestinationToDistanceInMForSource1 = new LinkedHashMap<>();
    mapDestinationToDistanceInMForSource1.put(2, 1063);
    Map<Integer, Integer> mapDestinationToDistanceInMForSource2 = new LinkedHashMap<>();
    mapDestinationToDistanceInMForSource2.put(0, 844);
    mapDestinationToDistanceInMForSource2.put(1, 1063);
    Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM = new LinkedHashMap<>();
    mapSrcToMapDestinationToDistanceInM.put(0, mapDestinationToDistanceInMForSource0);
    mapSrcToMapDestinationToDistanceInM.put(1, mapDestinationToDistanceInMForSource1);
    mapSrcToMapDestinationToDistanceInM.put(2, mapDestinationToDistanceInMForSource2);

    when(distancesService.getMapScrToMapDestinationToDistanceInM(any())).thenReturn(mapSrcToMapDestinationToDistanceInM);
  }

  @Test
  public void getGraphReturnsCorrectGraph() throws IOException {
    mockInjections();

    List<List<Edge>> expectedResult = List.of(
        List.of(
            new Edge(2, 844)
        ),
        List.of(
            new Edge(2, 1063)
        ),
        List.of(
            new Edge(0, 844),
            new Edge(1, 1063)
        )
    );
    List<List<Edge>> actualResult = new GraphService(stationRepository, distancesService).getGraph();

    assertEquals(expectedResult, actualResult);
  }
}
