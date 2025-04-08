package com.emersondev.api.controller;

import com.emersondev.api.request.LoginRequest;
import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.JwtResponse;
import com.emersondev.api.response.UserResponse;
import com.emersondev.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    JwtResponse response = authService.authenticateUser(loginRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
    UserResponse response = authService.registerUser(userRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/check-email")
  public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
    Boolean exists = authService.existsByEmail(email);
    return ResponseEntity.ok(exists);
  }
}