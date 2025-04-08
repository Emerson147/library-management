package com.emersondev.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
  private static final DateTimeFormatter DATE_FORMATTER =
          DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
          DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_FORMAT);

  private DateUtils() {
    // Private constructor to prevent instantiation
  }

  public static String formatDate(LocalDate date) {
    if (date == null) return null;
    return date.format(DATE_FORMATTER);
  }

  public static String formatDateTime(LocalDateTime dateTime) {
    if (dateTime == null) return null;
    return dateTime.format(DATE_TIME_FORMATTER);
  }

  public static LocalDate parseDate(String dateStr) {
    if (dateStr == null || dateStr.isBlank()) return null;
    return LocalDate.parse(dateStr, DATE_FORMATTER);
  }

  public static LocalDateTime parseDateTime(String dateTimeStr) {
    if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
    return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
  }

  public static boolean isValidLoanPeriod(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) return false;

    // Loan period should be between 1 day and 30 days
    long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    return days >= 1 && days <= 30;
  }

  public static LocalDate calculateDefaultDueDate(LocalDate loanDate) {
    // Default loan period is 14 days
    return loanDate.plusDays(14);
  }
}
