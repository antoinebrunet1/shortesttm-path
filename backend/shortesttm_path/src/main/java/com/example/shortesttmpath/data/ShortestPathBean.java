package com.example.shortesttmpath.data;

import com.example.shortesttmpath.enums.Station;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the shortest metro path between two STM metro stations.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ShortestPathBean {
  /**
   * The default constructor.
   */
  public ShortestPathBean() {
  }

  NonEndingStationInPathBean startingStation;
  Station destinationStation;
  List<NonEndingStationInPathBean> stationsToSwitchLines;
}
