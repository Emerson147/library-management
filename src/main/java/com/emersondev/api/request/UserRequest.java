package com.emersondev.api.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
          message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
  private String password;

  @Past(message = "Date of birth must be in the past")
  private LocalDate dateOfBirth;

  @NotBlank(message = "Address is required")
  private String address;

  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
  private String phoneNumber;

  @NotEmpty(message = "At least one role is required")
  private Set<String> roles;
}
