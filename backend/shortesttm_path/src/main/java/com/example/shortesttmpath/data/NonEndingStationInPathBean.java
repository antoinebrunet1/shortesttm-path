package com.example.shortesttmpath.data;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonEndingStationInPathBean {
  String name;
  String line;
  String direction;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof NonEndingStationInPathBean that)) {
      return false;
    }
    return Objects.equals(name, that.name) && Objects.equals(line, that.line) &&
        Objects.equals(direction, that.direction);
  }
}
