package com.example.shortesttm_path.exception;

public class StationsOnSameLineException extends RuntimeException {
    public StationsOnSameLineException() {
        super("Provided stations %s and %s are on the same line");
    }
}
