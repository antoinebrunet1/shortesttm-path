package com.example.shortesttmpath.exception;

/**
 * Is thrown when the two stations are on the same line. This includes the same station given twice and neighbor
 * stations.
 */
public class StationsOnSameLineException extends RuntimeException {
    /**
     * Standard RuntimeException constructor with a custom message.
     */
    public StationsOnSameLineException() {
        super("Provided stations are on the same line");
    }
}
