package com.example.shortesttmpath.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonEndingStationInPath {
  String name;
  String line;
  String direction;
}
