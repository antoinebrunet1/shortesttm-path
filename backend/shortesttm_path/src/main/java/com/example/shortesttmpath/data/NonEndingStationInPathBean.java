package com.example.shortesttmpath.data;

import com.example.shortesttmpath.enums.Line;
import com.example.shortesttmpath.enums.Station;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents any station in a shortest path that is not the destination station.
 */
@Data
@AllArgsConstructor
public class NonEndingStationInPathBean {
  Station name;
  Line line;
  Station direction;

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
