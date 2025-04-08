package com.emersondev.service.impl;

import com.emersondev.api.request.BookRequest;
import com.emersondev.api.response.BookResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.domain.entity.Book;
import com.emersondev.domain.entity.Category;
import com.emersondev.domain.exception.BookNotFoundException;
import com.emersondev.domain.exception.ResourceNotFoundException;
import com.emersondev.domain.repository.BookRepository;
import com.emersondev.domain.repository.CategoryRepository;
import com.emersondev.mapper.BookMapper;
import com.emersondev.service.interfaces.BookService;
import com.emersondev.util.AppConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final BookMapper bookMapper;

  public BookServiceImpl(BookRepository bookRepository,
                         CategoryRepository categoryRepository,
                         BookMapper bookMapper) {
    this.bookRepository = bookRepository;
    this.categoryRepository = categoryRepository;
    this.bookMapper = bookMapper;
  }

  @Override
  public PagedResponse<BookResponse> getAllBooks(int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Book> books = bookRepository.findAll(pageable);

    return getPagedResponse(books);
  }

  private PagedResponse<BookResponse> getPagedResponse(Page<Book> books) {
    List<BookResponse> content = books.getContent().stream()
            .map(bookMapper::toResponse)
            .collect(Collectors.toList());

    return new PagedResponse<>(
            content,
            books.getNumber(),
            books.getSize(),
            books.getTotalElements(),
            books.getTotalPages(),
            books.isLast());
  }

  @Override
  public BookResponse getBookById(Long id) {
    Book book = bookRepository.findById(id).
            orElseThrow(() -> new BookNotFoundException("id", id));
    return bookMapper.toResponse(book);
  }

  @Override
  public BookResponse getBookByIsbn(String isbn) {
    Book book = bookRepository.findByIsbn(isbn)
            .orElseThrow(() -> new BookNotFoundException("isbn", isbn));
    return bookMapper.toResponse(book);
  }

  @Override
  @Transactional
  public BookResponse createBook(BookRequest bookRequest) {
    //Check if ISBN already exists
    if (bookRepository.findByIsbn(bookRequest.getIsbn()).isPresent()) {
      throw new RuntimeException("Book with ISBN " + bookRequest.getIsbn() + " already exists");
    }

    //Find Category
    Category category = categoryRepository.findById(bookRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + bookRequest.getCategoryId()));

    //Create and save book
    Book book = bookMapper.toEntity(bookRequest, category);
    book = bookRepository.save(book);

    return bookMapper.toResponse(book);
  }

  @Override
  public BookResponse updateBook(Long id, BookRequest bookRequest) {
    // FInd book to update
    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("id", id));

    // Check if ISBN already exists
    if (bookRepository.findByIsbn(bookRequest.getIsbn()).isPresent()) {
      throw new RuntimeException("Book with ISBN " + bookRequest.getIsbn() + " already exists.");
    }

    //Find category
    Category category = categoryRepository.findById(bookRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + bookRequest.getCategoryId()));

    //Update book
    bookMapper.updateEntity(book, bookRequest, category);
    book = bookRepository.save(book);

    return bookMapper.toResponse(book);
  }

  @Override
  public void deleteBook(Long id) {
    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("id", id));

    //Check if book has active laans
    if (book.getLoans().stream().anyMatch(loan -> !loan.getIsReturned())) {
      throw new RuntimeException("Cannot delete book with active loans");
    }

    bookRepository.delete(book);
  }

  @Override
  public PagedResponse<BookResponse> searchBooks(String query, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
    Page<Book> books = bookRepository.search(query, pageable);

    return getPagedResponse(books);
  }

  @Override
  public PagedResponse<BookResponse> getBooksByCategory(Long categoryId, int page, int size) {
    // First, verify the category exists
    categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

    Pageable pageable = PageRequest.of(page, size, Sort.by(AppConstants.DEFAULT_SORT_BY).descending());
    Page<Book> books = bookRepository.findByCategoryId(categoryId, pageable);
    return getPagedResponse(books);
  }

  @Override
  public long countAvailableBooks() {
    return bookRepository.countAvailableBooks();
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    return bookRepository.findByIsbn(isbn).isPresent();
  }
}
