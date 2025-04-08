package com.emersondev.mapper;

import com.emersondev.api.request.BookRequest;
import com.emersondev.api.response.BookResponse;
import com.emersondev.domain.entity.Book;
import com.emersondev.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  public Book toEntity(BookRequest request, Category category) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setIsbn(request.getIsbn());
    book.setPublicationDate(request.getPublicationDate());
    book.setDescription(request.getDescription());
    book.setCategory(category);
    book.setTotalCopies(request.getTotalCopies());
    book.setAvailableCopies(request.getTotalCopies());
    return book;
  }

  public void updateEntity(Book book, BookRequest request, Category category) {
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setIsbn(request.getIsbn());
    book.setPublicationDate(request.getPublicationDate());
    book.setDescription(request.getDescription());
    book.setCategory(category);

    // Handle copies update carefully to not lose track of available copies
    Integer newTotal = request.getTotalCopies();
    Integer currentAvailable = book.getAvailableCopies();
    Integer currentTotal = book.getTotalCopies();

    //Calculate the difference between the new total and the current total
    Integer delta = newTotal - currentTotal;

    book.setTotalCopies(newTotal);
    // Adjust available copies based on the delta, but don't go below 0
    book.setAvailableCopies(Math.max(0, currentAvailable + delta));
  }

  public BookResponse toResponse(Book book) {
    return BookResponse.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(book.getAuthor())
            .isbn(book.getIsbn())
            .publicationDate(book.getPublicationDate())
            .description(book.getDescription())
            .categoryName(book.getCategory().getName())
            .categoryId(book.getCategory().getId())
            .totalCopies(book.getTotalCopies())
            .availableCopies(book.getAvailableCopies())
            .totalCopies(book.getTotalCopies())
            .available(book.isAvailable())
            .build();
  }


}
