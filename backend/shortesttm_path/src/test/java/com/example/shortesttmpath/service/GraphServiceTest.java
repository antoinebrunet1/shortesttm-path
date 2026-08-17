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
    Map<Integer, Map<Integer, Integer>> mapSrcToMapDestinationToDistanceInM = new LinkedHashMap<>(Map.of(
        0, new LinkedHashMap<>(Map.of(
            2, 844
        )),
        2, new LinkedHashMap<>(Map.of(
            0, 844,
            1, 1063
        )),
        1, new LinkedHashMap<>(Map.of(
            2, 1063))
    ));
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
            new Edge(1, 1063),
            new Edge(0, 844)
        )
    );
    List<List<Edge>> actualResult = new GraphService(stationRepository, distancesService).getGraph();

    assertEquals(expectedResult, actualResult);
  }
}
