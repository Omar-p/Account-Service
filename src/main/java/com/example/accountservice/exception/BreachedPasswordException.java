package com.example.accountservice.exception;

public class BreachedPasswordException extends RuntimeException {
  public BreachedPasswordException(String message) {
    super(message);
  }
}
