package com.example.shortesttmpath.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DistancesServiceTest {
  @Mock
  FileService fileService;
  @InjectMocks
  DistancesService distancesService;

  @Test
  public void getMapScrToMapDestinationToDistanceInMHappyPath() throws IOException {
    Map<String, Integer> stationsNamesToInts = Map.of(
        "Angrignon", 0,
        "Jolicoeur", 1,
        "Monk", 2
    );
    List<String> distances = List.of(
        "Angrignon to Monk\t:\t844",
        "Monk to Jolicoeur\t:\t1063"
    );

    when(fileService.getLines(any())).thenReturn(distances);

    Map<Integer, Map<Integer, Integer>> expectedResult = Map.of(
        0, Map.of(
            2, 844
        ),
        2, Map.of(
            0, 844,
            1, 1063
        ),
        1, Map.of(
            2, 1063)
    );
    Map<Integer, Map<Integer, Integer>> actualResult = distancesService.getMapScrToMapDestinationToDistanceInM(stationsNamesToInts);

    Assertions.assertEquals(expectedResult, actualResult);
  }
}
