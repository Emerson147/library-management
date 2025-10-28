package com.emersondev.mapper;

import com.emersondev.api.request.RoleRequest;
import com.emersondev.api.response.RoleResponse;
import com.emersondev.domain.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

  public Role toEntity(RoleRequest request) {
    Role role = new Role();
    role.setName(request.getName());
    role.setDescription(request.getDescription());
    return role;
  }

  public void updateEntity(Role role, RoleRequest request) {
    role.setName(request.getName());
    role.setDescription(request.getDescription());
  }

  public RoleResponse toResponse(Role role) {
    return RoleResponse.builder()
            .id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .createdAt(role.getCreatedAt())
            .updatedAt(role.getUpdatedAt())
            .createdBy(role.getCreatedBy())
            .updatedBy(role.getUpdatedBy())
            .build();
  }
}
