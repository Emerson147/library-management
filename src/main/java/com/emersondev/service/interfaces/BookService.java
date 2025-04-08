package com.emersondev.service.interfaces;

import com.emersondev.api.request.BookRequest;
import com.emersondev.api.response.BookResponse;
import com.emersondev.api.response.PagedResponse;

public interface BookService {
  PagedResponse<BookResponse> getAllBooks(int page, int size, String sortBy, String sortDir);
  BookResponse getBookById(Long id);
  BookResponse getBookByIsbn(String isbn);
  BookResponse createBook(BookRequest bookRequest);
  BookResponse updateBook(Long id, BookRequest bookRequest);
  void deleteBook(Long id);
  PagedResponse<BookResponse> searchBooks(String query, int page, int size);
  PagedResponse<BookResponse> getBooksByCategory(Long categoryId, int page, int size);
  long countAvailableBooks();
  boolean existsByIsbn(String isbn);
}
