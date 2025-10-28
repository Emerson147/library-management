package com.emersondev.service.interfaces;

import com.emersondev.api.request.RoleRequest;
import com.emersondev.api.response.RoleResponse;
import com.emersondev.api.response.PagedResponse;

public interface RoleService {

  RoleResponse createRole(RoleRequest roleRequest);
  RoleResponse getRoleById(Long id);
  RoleResponse updateRole(Long id, RoleRequest roleRequest);
  PagedResponse<RoleResponse> getAllRoles(int page, int size, String sortBy, String sortDir);
  void deleteRole(Long id);
}
