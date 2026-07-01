package com.example.shortesttmpath.data;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonEndingStationInPath {
  String name;
  String line;
  String direction;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof NonEndingStationInPath that)) {
      return false;
    }
    return Objects.equals(name, that.name) && Objects.equals(line, that.line) &&
        Objects.equals(direction, that.direction);
  }
}
