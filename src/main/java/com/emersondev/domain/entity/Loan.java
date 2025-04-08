package com.emersondev.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Loan extends Audit{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @Column(name = "loan_date", nullable = false)
  private LocalDate loanDate;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Column(name = "return_date")
  private LocalDate returnDate;

  @Column(name = "is_returned", nullable = false)
  private Boolean isReturned = false;

  @Column(name = "fine_amount")
  private Double fineAmount = 0.0;

  @Column(name = "fine_paid", nullable = false)
  private Boolean finePaid = false;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanStatus status = LoanStatus.ACTIVE;

  public enum LoanStatus {
    ACTIVE, OVERDUE, RETURNED, LOST
  }

  public boolean isOverdue() {
    if (isReturned) {
      return false;
    }
    return LocalDate.now().isAfter(dueDate);
  }

  public void markAsReturned() {
    this.returnDate = LocalDate.now();
    this.isReturned = true;
    this.status = LoanStatus.RETURNED;

    // Calculate fine if returned late
    if (this.returnDate.isAfter(this.dueDate)) {
      long daysLate = java.time.temporal.ChronoUnit.DAYS.between(this.dueDate, this.returnDate);
      this.fineAmount = daysLate * 0.50; // $0.50 per day late
    }
  }
}
