package com.emersondev.util;

public class AppConstants {

  // Pagination constants
  public static final String DEFAULT_PAGE_NUMBER = "0";
  public static final String DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_SORT_BY = "createdAt";
  public static final String DEFAULT_SORT_DIRECTION = "desc";

  // Security constants
  public static final long JWT_EXPIRATION = 86400000; // 24 hours in milliseconds
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";

  // Date patterns
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  // Role constants
  public static final String ROLE_USER = "USER";
  public static final String ROLE_LIBRARIAN = "LIBRARIAN";
  public static final String ROLE_ADMIN = "ADMIN";

  private AppConstants() {
    // Private constructor to prevent instantiation
  }
}
