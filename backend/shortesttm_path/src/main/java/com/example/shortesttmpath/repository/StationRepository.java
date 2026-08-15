package com.example.shortesttmpath.repository;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.util.FileUtil;
import java.io.IOException;
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

  public StationRepository() throws IOException {
  }
}
