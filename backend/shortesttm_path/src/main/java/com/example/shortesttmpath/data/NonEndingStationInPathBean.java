package com.example.shortesttmpath.data;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents any station in a shortest path that is not the destination station.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NonEndingStationInPathBean {
  Station name;
  Line line;
  Station direction;

  /**
   * The default constructor.
   */
  public NonEndingStationInPathBean() {
  }
}
