package com.emersondev.api.controller;

import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.api.response.UserResponse;
import com.emersondev.service.interfaces.UserService;
import com.emersondev.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
          @RequestParam(defaultValue = "id") String sortBy,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

    PagedResponse<UserResponse> response = userService.getAllUsers(page, size, sortBy, sortDir);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    UserResponse response = userService.getUserById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/email/{email}")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
    UserResponse response = userService.getUserByEmail(email);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
  public ResponseEntity<UserResponse> updateUser(
          @PathVariable Long id,
          @Valid @RequestBody UserRequest userRequest) {

    UserResponse response = userService.updateUser(id, userRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<UserResponse>> searchUsers(
          @RequestParam String query,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

    PagedResponse<UserResponse> response = userService.searchUsers(query, page, size);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}/activate")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> activateUser(@PathVariable Long id) {
    userService.activateUser(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/desactivate")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> desactivateUser(@PathVariable Long id) {
    userService.desactivateUser(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/active/count")
  @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
  public ResponseEntity<Long> countActiveMembers() {
    long count = userService.countActiveMembers();
    return ResponseEntity.ok(count);
  }


  
}
