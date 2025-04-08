package com.emersondev.service.interfaces;

import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.api.response.UserResponse;

public interface UserService {
  PagedResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir);
  UserResponse getUserById(Long id);
  UserResponse getUserByEmail(String email);
  UserResponse updateUser(Long id, UserRequest userRequest);
  void deleteUser(Long id);
  PagedResponse<UserResponse> searchUsers(String query, int page, int size);
  void activateUser(Long id);
  void desactivateUser(Long id);
  long countActiveMembers();
}
