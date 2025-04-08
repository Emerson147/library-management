package com.emersondev.api.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

  @NotNull(message = "User ID is required")
  private Long userId;

  @NotNull(message = "Book ID is required")
  private Long bookId;

  @NotNull(message = "Loan date is required")
  private LocalDate loanDate;

  @NotNull(message = "Due date is required")
  @Future(message = "Due date must be in the future")
  private LocalDate dueDate;
}
