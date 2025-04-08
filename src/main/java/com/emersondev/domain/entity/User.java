package com.emersondev.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "loans"})
public class User extends Audit{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(nullable = false)
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "membership_date")
  private LocalDate membershipDate;

  @Column(name = "membership_status", nullable = false)
  private Boolean membershipStatus = true;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @ToString.Exclude
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  private Set<Loan> loans = new HashSet<>();
}
