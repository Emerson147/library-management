package com.emersondev.service.impl;

import com.emersondev.api.request.LoanRequest;
import com.emersondev.api.response.LoanResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.domain.entity.Book;
import com.emersondev.domain.entity.Loan;
import com.emersondev.domain.entity.User;
import com.emersondev.domain.exception.LoanException;
import com.emersondev.domain.exception.ResourceNotFoundException;
import com.emersondev.domain.repository.BookRepository;
import com.emersondev.domain.repository.LoanRepository;
import com.emersondev.domain.repository.UserRepository;
import com.emersondev.mapper.LoanMapper;
import com.emersondev.service.interfaces.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

  private final LoanRepository loanRepository;
  private final UserRepository userRepository;
  private final BookRepository bookRepository;
  private final LoanMapper loanMapper;

  public LoanServiceImpl(LoanRepository loanRepository,
                         UserRepository userRepository,
                         BookRepository bookRepository,
                         LoanMapper loanMapper) {
    this.loanRepository = loanRepository;
    this.userRepository = userRepository;
    this.bookRepository = bookRepository;
    this.loanMapper = loanMapper;
  }

  @Override
  @Transactional
  public LoanResponse createLoan(LoanRequest loanRequest) {
    //FINd User
    User user = userRepository.findById(loanRequest.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", loanRequest.getUserId()));

    //Verify user Active
    if (!user.getMembershipStatus()) {
      throw new IllegalStateException("User membership is not active");
    }

    //Check if user has unpaid fines
    Double unpaidFines = loanRepository.getTotalUnpaidFinesForUser(loanRequest.getUserId());
    if (unpaidFines != null && unpaidFines > 0) {
        throw new LoanException("User has no unpaid fine of $" + unpaidFines + ". Please clear dues before new loan.");
    }

    // Find Book
    Book book = bookRepository.findById(loanRequest.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", loanRequest.getBookId()));

    // Check if book is available
    if (!book.isAvailable()) {
      throw new LoanException("Book is not available for loan");
    }

    //Check if due date is valid (not in past and not more than 30 days)
    if (loanRequest.getDueDate().isBefore(LocalDate.now())) {
      throw new LoanException("Due date cannot be in the past");
    }

    if (loanRequest.getDueDate().isAfter(LocalDate.now().plusMonths(3))) {
      throw new LoanException("Due date cannot be more than 3 months from now");
    }

    // Asegurar que loanDate siempre tenga valor
    if (loanRequest.getLoanDate() == null) {
      loanRequest.setLoanDate(LocalDate.now());
    }

    //Create Loan
    Loan loan = loanMapper.toEntity(loanRequest, user, book);

    // Reduce available copies count
    book.decrementAvailableCopies();
    bookRepository.save(book);

    // Save the loan
    loan = loanRepository.save(loan);

    return loanMapper.toResponse(loan);

  }

  @Override
  public PagedResponse<LoanResponse> getAllLoans(int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Loan> loans = loanRepository.findAll(pageable);

    return getPagedResponse(loans);
  }

  private PagedResponse<LoanResponse> getPagedResponse(Page<Loan> loans) {
    List<LoanResponse> content = loans.getContent().stream()
            .map(loanMapper::toResponse)
            .collect(Collectors.toList());
    return new PagedResponse<>(
            content,
            loans.getNumber(),
            loans.getSize(),
            loans.getTotalElements(),
            loans.getTotalPages(),
            loans.isLast()
    );
  }

  @Override
  public LoanResponse getLoanById(Long id) {
    Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));
    return loanMapper.toResponse(loan);
  }

  @Override
  @Transactional
  public LoanResponse returnBook(Long id) {
    Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));

    //Check if book is already returned
    if (loan.getIsReturned()) {
      throw new LoanException("Book is already returned");
    }

    //Mark book as returned
    loan.markAsReturned();

    //Increment available copies of book
    Book book = loan.getBook();
    book.incrementAvailableCopies();
    bookRepository.save(book);

    //Save the loan
    loan = loanRepository.save(loan);

    return loanMapper.toResponse(loan);

  }

  @Override
  @Transactional
  public void deleteLoan(Long id) {
    Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", id));

    //Check if book is already returned
    if (!loan.getIsReturned()) {
      throw new LoanException("No se puede eliminar un prestamo activo. El libro debe ser devuelto primero");
    }

    //Delete the loan
    loanRepository.delete(loan);

  }

  @Override
  public PagedResponse<LoanResponse> getLoansByUser(Long userId, int page, int size) {
    // Verify if users exists
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Pageable pageable = PageRequest.of(page, size, Sort.by("loanDate").descending());
    Page<Loan> loans = loanRepository.findByUser(user, pageable);

    return getPagedResponse(loans);
  }

  @Override
  public PagedResponse<LoanResponse> getOverdueLoans(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
    Page<Loan> overdueLoans = loanRepository.findOverdueLoans(pageable);

    return getPagedResponse(overdueLoans);
  }

  @Override
  @Transactional
  public void payFine(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan", "id", loanId));

    if(loan.getFineAmount() <= 0 ) {
      throw new LoanException("No fines to pay for this loan.");
    }

    if (loan.getFinePaid()) {
      throw new LoanException("Fine is already paid for this loan.");
    }

    loan.setFinePaid(true);
    loanRepository.save(loan);
  }

  @Override
  public Double getTotalUnpaidFinesForUser(Long userId) {
    //Verify users exists
    userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    Double fines = loanRepository.getTotalUnpaidFinesForUser(userId);
    return fines != null ? fines : 0.0;
  }

  @Override
  public long countActiveLoansForUser(Long userId) {
    //Verify if user exists
    userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    return loanRepository.countActiveLoansForUser(userId);
  }
}
