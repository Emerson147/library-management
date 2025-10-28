package com.emersondev.service.impl;

import com.emersondev.api.request.RoleRequest;
import com.emersondev.api.response.RoleResponse;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.domain.entity.Role;
import com.emersondev.domain.repository.RoleRepository;
import com.emersondev.domain.repository.UserRepository;
import com.emersondev.mapper.RoleMapper;
import com.emersondev.service.interfaces.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;
  private final UserRepository userRepository;

  public RoleServiceImpl(RoleRepository roleRepository,
                         RoleMapper roleMapper,
                         UserRepository userRepository) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public RoleResponse createRole(RoleRequest roleRequest) {
    Role role = roleMapper.toEntity(roleRequest);
    Role savedRole = roleRepository.save(role);
    return roleMapper.toResponse(savedRole);
  }

  @Override
  public PagedResponse<RoleResponse> getAllRoles(int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Role> roles = roleRepository.findAll(pageable);

    List<RoleResponse> content = roles.getContent().stream()
            .map(roleMapper::toResponse)
            .collect(Collectors.toList());

    return new PagedResponse<>(
            content,
            roles.getNumber(),
            roles.getSize(),
            roles.getTotalElements(),
            roles.getTotalPages(),
            roles.isLast());
  }

  @Override
  public RoleResponse getRoleById(Long id) {
    Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

    return roleMapper.toResponse(role);
  }

  @Override
  @Transactional
  public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
    Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

    roleMapper.updateEntity(role, roleRequest);

    Role updatedRole = roleRepository.save(role);
    return roleMapper.toResponse(updatedRole);
  }

  @Override
  @Transactional
  public void deleteRole(Long id) {
    Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

    // Check if there are any users associated with this role
    long associatedUsers = userRepository.countByRolesId(id);
    if (associatedUsers > 0) {
      throw new RuntimeException("Cannot delete role with associated users");
    }

    roleRepository.delete(role);
  }
}
