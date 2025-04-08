package com.emersondev.api.response;

import com.emersondev.domain.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {

  private Long id;
  private Long userId;
  private String userName;
  private Long bookId;
  private String bookTitle;
  private LocalDate loanDate;
  private LocalDate dueDate;
  private LocalDate returnDate;
  private Boolean isReturned;
  private Double fineAmount;
  private Boolean finePaid;
  private Loan.LoanStatus status;
  private Boolean isOverdue;
}
