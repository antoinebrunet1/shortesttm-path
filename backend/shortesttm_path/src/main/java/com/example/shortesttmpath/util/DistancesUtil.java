package com.example.shortesttmpath.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Util class to get a Map that maps every source node in the stations graph to a Map that maps each
 * destination node to the distance from the source node in meters.
 */
public class DistancesUtil {
  /**
   * Returns a Map that maps every source node in the stations graph to a Map that maps each
   * destination node to the distance from the source node in meters.
   *
   * @param stationsNamesToInts Maps stations names to ints.
   * @return A Map that maps every source node in the stations graph to a Map that maps each
   *        destination node to the distance from the source node in meters.
   * @throws IOException IOException.
   */
  public static Map<Integer, Map<Integer, Integer>> getMapScrToMapDestinationToDistanceInM(
      Map<String, Integer> stationsNamesToInts)
      throws IOException {
    Map<Integer, Map<Integer, Integer>> distancesMap = new LinkedHashMap<>();
    List<String> distancesLines = FileUtil.getLines("distances.txt");

    for (String distanceLine : distancesLines) {
      addDistance(distanceLine, distancesMap, stationsNamesToInts);
    }

    return distancesMap;
  }

  private static void addDistance(String distanceLine,
                           Map<Integer, Map<Integer, Integer>> distancesMap,
                                  Map<String, Integer> stationsNamesToInts) {
    int station1 = stationsNamesToInts.get(distanceLine.split(" to ")[0]);
    int station2 = stationsNamesToInts.get(distanceLine.split(" to ")[1].split("\\s:\\s")[0]);
    int distance = Integer.parseInt(distanceLine.split(" to ")[1].split("\\s:\\s")[1]);

    addDistance(station1, station2, distance, distancesMap);
    addDistance(station2, station1, distance, distancesMap);
  }

  private static void addDistance(int station1, int station2, int distance,
                           Map<Integer, Map<Integer, Integer>> distancesMap) {
    if (distancesMap.containsKey(station1)) {
      distancesMap.get(station1).put(station2, distance);
    } else {
      Map<Integer, Integer> station1Map = new LinkedHashMap<>();
      station1Map.put(station2, distance);
      distancesMap.put(station1, station1Map);
    }
  }
}
