package com.example.shortesttmpath.exception;

public class InvalidLineException extends RuntimeException {
  public InvalidLineException() {
    super("Provided line is invalid");
  }
}
