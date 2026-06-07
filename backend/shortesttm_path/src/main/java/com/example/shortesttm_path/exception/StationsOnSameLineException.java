package com.example.shortesttm_path.exception;

public class StationsOnSameLineException extends RuntimeException {
    public StationsOnSameLineException(String startStation, String destinationStation) {
        super(String.format("Provided stations %s and %s are on the same line", startStation, destinationStation));
    }
}
