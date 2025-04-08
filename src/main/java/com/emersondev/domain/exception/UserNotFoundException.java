package com.emersondev.domain.exception;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException(String fieldName, Object fieldValue) {
    super("User", fieldName, fieldValue);
  }
}
