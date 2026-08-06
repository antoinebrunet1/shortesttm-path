package com.example.shortesttmpath.exception;

/**
 * Is thrown when at least one of the stations is not valid.
 */
public class StationsNotValidException extends RuntimeException {
  /**
   * Standard RuntimeException constructor with a custom message.
   */
  public StationsNotValidException() {
    super("Provided stations are not valid");
  }
}
