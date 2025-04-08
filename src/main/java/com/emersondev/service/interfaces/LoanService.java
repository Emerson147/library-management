package com.emersondev.service.interfaces;

import com.emersondev.api.request.LoanRequest;
import com.emersondev.api.response.LoanResponse;
import com.emersondev.api.response.PagedResponse;

public interface LoanService {
  PagedResponse<LoanResponse> getAllLoans(int page, int size, String sortBy, String sortDir);
  LoanResponse getLoanById(Long id);
  LoanResponse createLoan(LoanRequest loanRequest);
  LoanResponse returnBook(Long id);
  void deleteLoan(Long id);
  PagedResponse<LoanResponse> getLoansByUser(Long userId, int page, int size);
  PagedResponse<LoanResponse> getOverdueLoans(int page, int size);
  void payFine(Long loanId);
  Double getTotalUnpaidFinesForUser(Long userId);
  long countActiveLoansForUser(Long userId);
}
