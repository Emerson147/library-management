package com.emersondev.mapper;

import com.emersondev.api.request.LoanRequest;
import com.emersondev.api.response.LoanResponse;
import com.emersondev.domain.entity.Book;
import com.emersondev.domain.entity.Loan;
import com.emersondev.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
  public Loan toEntity(LoanRequest request, User user, Book book) {
    Loan loan = new Loan();
    loan.setUser(user);
    loan.setBook(book);
    loan.setLoanDate(request.getLoanDate());
    loan.setDueDate(request.getDueDate());
    loan.setIsReturned(false);
    loan.setFineAmount(0.0);
    loan.setFinePaid(false);
    loan.setStatus(Loan.LoanStatus.ACTIVE);
    return loan;
  }

  public LoanResponse toResponse(Loan loan) {
    return LoanResponse.builder()
            .id(loan.getId())
            .userId(loan.getUser().getId())
            .userName(loan.getUser().getName())
            .bookId(loan.getBook().getId())
            .bookTitle(loan.getBook().getTitle())
            .loanDate(loan.getLoanDate())
            .dueDate(loan.getDueDate())
            .returnDate(loan.getReturnDate())
            .isReturned(loan.getIsReturned())
            .fineAmount(loan.getFineAmount())
            .finePaid(loan.getFinePaid())
            .status(loan.getStatus())
            .isOverdue(loan.isOverdue())
            .build();
  }
}
