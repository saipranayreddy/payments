package com.example.payments.exception;

public class ContentNotFoundException extends RuntimeException {

  public ContentNotFoundException(String message) {
    super(message);
  }

  public ContentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }


}
