package com.example.shortesttm_path.util;

import com.example.shortesttm_path.data.ShortestPathBean;
import com.example.shortesttm_path.exception.StationsOnSameLineException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortestPathUtilTest {
    @Test
    public void getShortestPathHappyPath() {
        String inputStartingStation = "Laurier";
        String inputDestinationStation = "Charlevoix";
        List<String> expectedStationsToSwitchLines = List.of("Berri-UQAM");
        ShortestPathBean actualPath = ShortestPathUtil.getShortestPath(inputStartingStation, inputDestinationStation);

        assertEquals(inputStartingStation, actualPath.getStartingStation());
        assertEquals(inputDestinationStation, actualPath.getDestinationStation());
        assertEquals(expectedStationsToSwitchLines, actualPath.getStationsToSwitchLines());
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        assertThrows(StationsOnSameLineException.class, () ->
            ShortestPathUtil.getShortestPath("McGill", "Viau"));
    }
}
