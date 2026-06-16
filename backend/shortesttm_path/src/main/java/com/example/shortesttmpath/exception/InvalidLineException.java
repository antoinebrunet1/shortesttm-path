package com.example.shortesttmpath.exception;

/**
 * Is thrown when the provided metro line is invalid.
 */
public class InvalidLineException extends RuntimeException {
  /**
   * Standard RuntimeException constructor with a custom message.
   */
  public InvalidLineException() {
    super("Provided line is invalid");
  }
}
