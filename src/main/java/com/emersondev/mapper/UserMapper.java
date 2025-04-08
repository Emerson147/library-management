package com.emersondev.mapper;

import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.UserResponse;
import com.emersondev.domain.entity.Role;
import com.emersondev.domain.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

  private final PasswordEncoder passwordEncoder;

  public UserMapper(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public User toEntity(UserRequest request, Set<Role> roles) {
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setDateOfBirth(request.getDateOfBirth());
    user.setAddress(request.getAddress());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setMembershipDate(LocalDate.now());
    user.setMembershipStatus(true);
    user.setRoles(roles);
    return user;
  }

  public void updateEntity(User user, UserRequest request, Set<Role> roles) {
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    user.setDateOfBirth(request.getDateOfBirth());
    user.setAddress(request.getAddress());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setRoles(roles);
  }

  public UserResponse toResponse(User user, int activeLoans) {
    return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .dateOfBirth(user.getDateOfBirth())
            .address(user.getAddress())
            .phoneNumber(user.getPhoneNumber())
            .membershipDate(user.getMembershipDate())
            .membershipStatus(user.getMembershipStatus())
            .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
            .activeLoans(activeLoans)
            .build();
  }
}