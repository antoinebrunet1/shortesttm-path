package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.util.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class StationRepository {
  private final Map<Line, List<String>> linesToStations = Map.of(
      Line.BLUE, FileUtil.getLines("blue_line_stations.txt"),
      Line.GREEN, FileUtil.getLines("green_line_stations.txt"),
      Line.ORANGE, FileUtil.getLines("orange_line_stations.txt"),
      Line.YELLOW, FileUtil.getLines("yellow_line_stations.txt")
  );
  private final List<String> allStationsAlphabeticalOrder =
      FileUtil.getLines("all_stations_alphabetical_order.txt");
  private final List<String> allStationsToSwitchLines =
      FileUtil.getLines("all_stations_to_switch_lines.txt");
  private Map<String, Integer> stationsNamesToInts;
  private Map<Integer, String> intsToStationsNames;

  /**
   * "set" methods used instead of "get" methods to differentiate from the Lombok getters.
   *
   * @throws IOException IOException.
   */
  public StationRepository() throws IOException {
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

  public List<Line> getLines(String station) {
    List<Line> lines = new ArrayList<>();

    for (Line line : linesToStations.keySet()) {
      if (linesToStations.get(line).contains(station)) {
        lines.add(line);
      }
    }

    return lines;
  }
}
