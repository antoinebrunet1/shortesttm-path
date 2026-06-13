package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortestPathUtilTest {
    @Test
    public void getShortestPathHappyPath() {
        String inputStartingStation = "Laurier";
        String inputDestinationStation = "Charlevoix";
        ShortestPathBean actualPath = ShortestPathUtil.getShortestPath(inputStartingStation, inputDestinationStation);
        ShortestPathBean expectedPath = new ShortestPathBean(
                inputStartingStation,
                inputDestinationStation,
                List.of("Berri-UQAM"));

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        assertThrows(StationsOnSameLineException.class, () ->
            ShortestPathUtil.getShortestPath("McGill", "Viau"));
    }
}
