package com.emersondev.api.controller;

import com.emersondev.api.request.RoleRequest;
import com.emersondev.api.response.RoleResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.service.interfaces.RoleService;
import com.emersondev.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PagedResponse<RoleResponse>> getAllRoles(
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
          @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
          @RequestParam(defaultValue = "name") String sortBy,
          @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

    PagedResponse<RoleResponse> response = roleService.getAllRoles(page, size, sortBy, sortDir);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
    RoleResponse response = roleService.createRole(roleRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
    RoleResponse response = roleService.getRoleById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RoleResponse> updateRole(
          @PathVariable Long id,
          @Valid @RequestBody RoleRequest roleRequest) {
    RoleResponse response = roleService.updateRole(id, roleRequest);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
    roleService.deleteRole(id);
    return ResponseEntity.noContent().build();
  }
}
