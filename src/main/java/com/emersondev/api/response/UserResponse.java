package com.emersondev.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String name;
  private String email;
  private LocalDate dateOfBirth;
  private String address;
  private String phoneNumber;
  private LocalDate membershipDate;
  private Boolean membershipStatus;
  private Set<String> roles;
  private int activeLoans;
}
