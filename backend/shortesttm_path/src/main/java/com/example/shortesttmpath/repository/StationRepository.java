package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.service.FileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Station related repository.
 */
@Getter
public class StationRepository {
  private final FileService fileService;
  private final Map<Line, List<String>> linesToStations;
  private final List<String> allStationsAlphabeticalOrder;
  private final List<String> allStationsToSwitchLines;
  private Map<String, Integer> stationsNamesToInts;
  private Map<Integer, String> intsToStationsNames;

  /**
   * "set" methods used instead of "get" methods to differentiate from the Lombok getters.
   *
   * @throws IOException IOException.
   */
  public StationRepository(FileService fileService) throws IOException {
    this.fileService = fileService;
    linesToStations = Map.of(
        Line.BLUE, fileService.getLines("blue_line_stations.txt"),
        Line.GREEN, fileService.getLines("green_line_stations.txt"),
        Line.ORANGE, fileService.getLines("orange_line_stations.txt"),
        Line.YELLOW, fileService.getLines("yellow_line_stations.txt")
    );
    allStationsAlphabeticalOrder =
        fileService.getLines("all_stations_alphabetical_order.txt");
    allStationsToSwitchLines =
        fileService.getLines("all_stations_to_switch_lines.txt");
    setStationsNamesToInts();
    setIntsToStationsNames();
  }

  private void setStationsNamesToInts() {
    Map<String, Integer> stationNamesToInts = new LinkedHashMap<>();

    for (int i = 0; i < allStationsAlphabeticalOrder.size(); i++) {
      stationNamesToInts.put(allStationsAlphabeticalOrder.get(i), i);
    }

    this.stationsNamesToInts = stationNamesToInts;
  }

  private void setIntsToStationsNames() {
    Map<Integer, String> intsToStationsNames = new LinkedHashMap<>();

    for (String station : stationsNamesToInts.keySet()) {
      intsToStationsNames.put(stationsNamesToInts.get(station), station);
    }

    this.intsToStationsNames = intsToStationsNames;
  }
}
