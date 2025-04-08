package com.emersondev.domain.exception;

public class BookNotFoundException extends ResourceNotFoundException {
  public BookNotFoundException(String fieldName, Object fieldValue) {
    super("Book", fieldName, fieldValue);
  }
}
