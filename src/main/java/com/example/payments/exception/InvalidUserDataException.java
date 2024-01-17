package com.example.payments.exception;

public class InvalidUserDataException extends RuntimeException {

  public InvalidUserDataException(String message) {
    super(message);
  }

  public InvalidUserDataException(String message, Throwable cause) {
    super(message, cause);
  }

}
