package com.emersondev.service.interfaces;

import com.emersondev.api.request.LoginRequest;
import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.JwtResponse;
import com.emersondev.api.response.UserResponse;
import jakarta.validation.Valid;

public interface AuthService {

  JwtResponse authenticateUser(LoginRequest loginRequest);
  UserResponse registerUser(@Valid UserRequest userRequest);
  boolean existsByEmail(String email);

}
