package com.example.shortesttm_path.util;

import com.example.shortesttm_path.data.ShortestPathBean;
import com.example.shortesttm_path.exception.StationsOnSameLineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ShortestPathUtilTest {
    @Test
    public void getShortestPathHappyPath() {
        String inputStartingStation = "Laurier";
        String inputDestinationStation = "Charlevoix";
        List<String> expectedStationsToSwitchLines = List.of("Berri-UQAM");
        ShortestPathBean actualPath = ShortestPathUtil.getShortestPath(inputStartingStation, inputDestinationStation);

        Assertions.assertEquals(inputStartingStation, actualPath.getStartingStation());
        Assertions.assertEquals(inputDestinationStation, actualPath.getDestinationStation());
        Assertions.assertEquals(expectedStationsToSwitchLines, actualPath.getStationsToSwitchLines());
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        Assertions.assertThrows(StationsOnSameLineException.class, () -> {
            ShortestPathUtil.getShortestPath("McGill", "Viau");
        });
    }
}
