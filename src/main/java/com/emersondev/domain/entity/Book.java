package com.emersondev.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false, unique = true)
  private String isbn;

  @Column(name = "publication_date")
  private LocalDate publicationDate;

  @Column(length = 1000)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(name = "available_copies")
  private Integer availableCopies;

  @Column(name = "total_copies")
  private Integer totalCopies;

  @OneToMany(mappedBy = "book")
  private Set<Loan> loans = new HashSet<>();

  public boolean isAvailable() {
    return availableCopies > 0;
  }

  public void decrementAvailableCopies() {
    if (availableCopies > 0) {
      availableCopies--;
    }
  }

  public void incrementAvailableCopies() {
    if (availableCopies < totalCopies) {
      availableCopies++;
    }
  }
}
