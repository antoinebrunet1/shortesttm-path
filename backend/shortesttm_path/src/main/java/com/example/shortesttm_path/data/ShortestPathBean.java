package com.example.shortesttm_path.data;

import lombok.Data;

import java.util.List;

/**
 * Represents the shortest metro path (the one with the fewer stations) between two STM metro stations.
 */
@Data
public class ShortestPathBean {
    /**
     * The default constructor.
     */
    public ShortestPathBean() {
    }

    String startingStation;
    String destinationStation;
    List<String> stationsToSwitchLines;
}
