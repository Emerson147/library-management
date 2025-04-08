package com.emersondev.domain.repository;

import com.emersondev.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByIsbn(String isbn);

  Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

  Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

  @Query("SELECT b FROM Book b WHERE " +
          "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  Page<Book> search(@Param("searchTerm") String searchTerm, Pageable pageable);

  @Query("SELECT COUNT(b) FROM Book b WHERE b.availableCopies > 0")
  long countAvailableBooks();

  @Query("SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId")
  long countByCategoryId(Long categoryId);
}
