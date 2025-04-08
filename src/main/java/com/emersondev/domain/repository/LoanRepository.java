package com.emersondev.domain.repository;

import com.emersondev.domain.entity.Book;
import com.emersondev.domain.entity.Loan;
import com.emersondev.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

  Page<Loan> findByUser(User user, Pageable pageable);

  Page<Loan> findByBook(Book book, Pageable pageable);

  List<Loan> findByDueDateBeforeAndIsReturnedFalse(LocalDate date);

  @Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.user.id = :userId")
  Page<Loan> findActiveLoansForUser(Long userId, Pageable pageable);

  @Query("SELECT COUNT(l) FROM Loan l WHERE l.isReturned = false AND l.user.id = :userId")
  long countActiveLoansForUser(Long userId);

  @Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.dueDate < CURRENT_DATE")
  Page<Loan> findOverdueLoans(Pageable pageable);

  @Query("SELECT COUNT(l) FROM Loan l WHERE l.book.id = :bookId AND l.isReturned = false")
  long countActiveLoansForBook(Long bookId);

  @Query("SELECT SUM(l.fineAmount) FROM Loan l WHERE l.user.id = :userId AND l.finePaid = false")
  Double getTotalUnpaidFinesForUser(Long userId);

}
