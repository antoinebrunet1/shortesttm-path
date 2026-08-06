package com.example.shortesttmpath.util;

import com.example.shortesttmpath.data.NonEndingStationInPathBean;
import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.exception.StationsNotValidException;
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
                new NonEndingStationInPathBean(
                    inputStartingStation,
                    "ORANGE",
                    "Côte-Vertu"
                ),
                inputDestinationStation,
            List.of(new NonEndingStationInPathBean(
                "Lionel-Groulx",
                "GREEN",
                "Angrignon"
            )));

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void getShortestPathSameLineShouldThrowStationsOnSameLineException() {
        assertThrows(StationsOnSameLineException.class, () ->
            ShortestPathUtil.getShortestPath("McGill", "Viau"));
    }

  @Test
  public void getShortestPathInvalidStartingStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        ShortestPathUtil.getShortestPath("MMcGill", "Viau"));
  }

  @Test
  public void getShortestPathInvalidDestinationStationShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        ShortestPathUtil.getShortestPath("McGill", "VViau"));
  }

  @Test
  public void getShortestPathInvalidStationsShouldThrowStationsNotValidException() {
    assertThrows(StationsNotValidException.class, () ->
        ShortestPathUtil.getShortestPath("MMcGill", "VViau"));
  }
}
