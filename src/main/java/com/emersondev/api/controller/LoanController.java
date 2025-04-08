package com.emersondev.api.controller;

import com.emersondev.api.request.LoanRequest;
import com.emersondev.api.response.LoanResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.service.interfaces.LoanService;
import com.emersondev.util.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

  private final LoanService loanService;

  public LoanController(LoanService loanService) {
    this.loanService = loanService;
  }

  // Add your endpoints here

  @GetMapping
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<LoanResponse>> getAllLoans(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
          @RequestParam(defaultValue = "loanDate") String sortBy,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

    PagedResponse<LoanResponse> response = loanService.getAllLoans(page, size, sortBy, sortDir);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<LoanResponse> createLoan(@RequestBody LoanRequest loanRequest) {
    LoanResponse response = loanService.createLoan(loanRequest);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
    LoanResponse response = loanService.getLoanById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}/return")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<LoanResponse> returnBook(@PathVariable Long id) {
    LoanResponse response = loanService.returnBook(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<LoanResponse> deleteLoan(@PathVariable Long id) {
    loanService.deleteLoan(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<LoanResponse>> getLoansByUser(
          @PathVariable Long userId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    PagedResponse<LoanResponse> response = loanService.getLoansByUser(userId, page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/overdue")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<LoanResponse>> getOverdueLoans(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    PagedResponse<LoanResponse> response = loanService.getOverdueLoans(page, size);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{id}/pay-fine")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<Void> payFine(@PathVariable Long id) {
    loanService.payFine(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/user/{userId}/unpaid-fines")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<Double> getTotalUnpaidFinesForUser(@PathVariable Long userId) {
    Double fines = loanService.getTotalUnpaidFinesForUser(userId);
    return ResponseEntity.ok(fines);
  }

  @GetMapping("/user/{userId}/active-loans/count")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<Long> countActiveLoansForUser(@PathVariable Long userId) {
    long count = loanService.countActiveLoansForUser(userId);
    return ResponseEntity.ok(count);
  }

}
