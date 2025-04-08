package com.emersondev.api.controller;

import com.emersondev.api.request.BookRequest;
import com.emersondev.api.response.BookResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.service.interfaces.BookService;
import com.emersondev.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<PagedResponse<BookResponse>> getAllBooks(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

    PagedResponse<BookResponse> response = bookService.getAllBooks(page, size, sortBy, sortDir);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
    BookResponse response = bookService.createBook(bookRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
    BookResponse response = bookService.getBookById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/isbn/{isbn}")
  public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
    BookResponse response = bookService.getBookByIsbn(isbn);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
    BookResponse response = bookService.updateBook(id, bookRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BookResponse> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<BookResponse>> searchBooks(
          @RequestParam String query,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
    PagedResponse<BookResponse> response = bookService.searchBooks(query, page, size);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/category/{categoryId}")
  public ResponseEntity<PagedResponse<BookResponse>> getBooksByCategory(
          @PathVariable Long categoryId,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    PagedResponse<BookResponse> response = bookService.getBooksByCategory(categoryId, page, size);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/available/count")
  public ResponseEntity<Long> countAvailableBooks() {
    long count = bookService.countAvailableBooks();
    return ResponseEntity.ok(count);
  }

  @GetMapping("/check-isbn")
  public ResponseEntity<Boolean> checkIsbnExists(@RequestParam String isbn) {
    Boolean exists = bookService.existsByIsbn(isbn);
    return ResponseEntity.ok(exists);
  }

}
