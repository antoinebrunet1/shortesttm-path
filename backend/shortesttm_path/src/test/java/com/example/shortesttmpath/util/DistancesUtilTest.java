package com.example.shortesttmpath.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class DistancesUtilTest {

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

    try (MockedStatic<FileUtil> utilities = Mockito.mockStatic(FileUtil.class)) {
      utilities.when(() -> FileUtil.getLines("distances.txt")).thenReturn(distances);

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
      Map<Integer, Map<Integer, Integer>> actualResult = DistancesUtil.getMapScrToMapDestinationToDistanceInM(stationsNamesToInts);
      Assertions.assertEquals(expectedResult, actualResult);
    }
  }
}
