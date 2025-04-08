package com.emersondev.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

  private Long id;
  private String title;
  private String author;
  private String isbn;
  private LocalDate publicationDate;
  private String description;
  private String categoryName;
  private Long categoryId;
  private Integer availableCopies;
  private Integer totalCopies;
  private boolean available;

}
