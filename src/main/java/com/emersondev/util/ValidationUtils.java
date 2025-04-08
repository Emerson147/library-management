package com.emersondev.util;

import java.util.regex.Pattern;

public class ValidationUtils {

  private static final Pattern ISBN_PATTERN = Pattern.compile(
          "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)" +
                  "(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
          "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

  private ValidationUtils() {
    // Private constructor to prevent instantiation
  }

  public static boolean isValidIsbn(String isbn) {
    if (isbn == null || isbn.isBlank()) return false;
    return ISBN_PATTERN.matcher(isbn).matches();
  }

  public static boolean isValidEmail(String email) {
    if (email == null || email.isBlank()) return false;
    return EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean isValidPassword(String password) {
    if (password == null || password.isBlank()) return false;
    return PASSWORD_PATTERN.matcher(password).matches();
  }

  public static boolean isValidPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.isBlank()) return false;
    return phoneNumber.matches("^\\+?[0-9]{10,15}$");
  }
}
