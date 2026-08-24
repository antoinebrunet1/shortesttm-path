package com.example.shortesttmpath.data;

import com.example.shortesttmpath.enums.Station;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the shortest metro path (the one with the least stations) between two STM metro
 * stations.
 */
@Data
@AllArgsConstructor
public class ShortestPathBean {
  /**
   * The default constructor.
   */
  public ShortestPathBean() {
  }

  NonEndingStationInPathBean startingStation;
  Station destinationStation;
  List<NonEndingStationInPathBean> stationsToSwitchLines;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ShortestPathBean that)) {
      return false;
    }
    return Objects.equals(startingStation, that.startingStation)
        && Objects.equals(destinationStation, that.destinationStation)
        && Objects.equals(stationsToSwitchLines, that.stationsToSwitchLines);
  }
}
