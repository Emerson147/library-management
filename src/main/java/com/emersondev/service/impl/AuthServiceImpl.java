package com.emersondev.service.impl;

import com.emersondev.api.request.LoginRequest;
import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.JwtResponse;
import com.emersondev.api.response.UserResponse;
import com.emersondev.domain.entity.Role;
import com.emersondev.domain.entity.User;
import com.emersondev.domain.repository.RoleRepository;
import com.emersondev.domain.repository.UserRepository;
import com.emersondev.mapper.UserMapper;
import com.emersondev.security.jwt.JwtProvider;
import com.emersondev.service.interfaces.AuthService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;

  public AuthServiceImpl(
          AuthenticationManager authenticationManager,
          JwtProvider jwtProvider,
          UserRepository userRepository,
          RoleRepository roleRepository,
          UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userMapper = userMapper;
  }

  @Override
  public JwtResponse authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtProvider.generateJwtToken(authentication);

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(role -> role.replace("ROLE_", ""))
            .collect(Collectors.toList());

    // Get user details from database
    User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));

    return new JwtResponse(
            jwt,
            user.getId(),
            user.getName(),
            user.getEmail(),
            roles
    );
  }

  @Override
  @Transactional
  public UserResponse registerUser(UserRequest userRequest) {
    if (userRepository.existsByEmail(userRequest.getEmail())) {
      throw new RuntimeException("Email is already in use!");
    }

    // Create user's roles
    Set<Role> roles = new HashSet<>();
    for (String roleName : userRequest.getRoles()) {
      Role role = roleRepository.findByName(roleName)
              .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
      // No modificar la colección de users del role
      roles.add(role);
    }

    // Create new user
    User user = userMapper.toEntity(userRequest, roles);
    user = userRepository.save(user);

    return userMapper.toResponse(user, 0);
  }
  
  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}