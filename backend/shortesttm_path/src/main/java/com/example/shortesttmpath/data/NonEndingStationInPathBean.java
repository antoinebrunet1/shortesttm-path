package com.example.shortesttmpath.data;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents any station in a shortest path that is not the destination station.
 */
@Data
@AllArgsConstructor
public class NonEndingStationInPathBean {
  String name;
  String line;
  String direction;

  /**
   * The default constructor.
   */
  public NonEndingStationInPathBean() {
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof NonEndingStationInPathBean that)) {
      return false;
    }
    return Objects.equals(name, that.name) && Objects.equals(line, that.line)
        && Objects.equals(direction, that.direction);
  }
}
