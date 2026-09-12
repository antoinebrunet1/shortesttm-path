package com.example.shortesttmpath.exception;

/**
 * Is thrown when at least one of the provided stations is invalid.
 */
public class AtLeastOneStationIsInvalidException extends RuntimeException {
  /**
   * Standard RuntimeException constructor with a custom message.
   */
  public AtLeastOneStationIsInvalidException() {
    super("At least one of the provided stations is invalid");
  }
}
