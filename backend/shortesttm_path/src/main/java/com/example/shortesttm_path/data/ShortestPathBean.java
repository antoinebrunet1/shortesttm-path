package com.example.shortesttm_path.data;

import lombok.Data;

import java.util.List;

@Data
public class ShortestPathBean {
    String startingStation;
    String destinationStation;
    List<String> stationsToSwitchLines;
}
