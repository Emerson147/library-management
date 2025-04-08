package com.emersondev.service.impl;

import com.emersondev.api.request.UserRequest;
import com.emersondev.api.response.PagedResponse;
import com.emersondev.api.response.UserResponse;
import com.emersondev.domain.entity.Role;
import com.emersondev.domain.entity.User;
import com.emersondev.domain.repository.LoanRepository;
import com.emersondev.domain.repository.RoleRepository;
import com.emersondev.domain.repository.UserRepository;
import com.emersondev.mapper.UserMapper;
import com.emersondev.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final LoanRepository loanRepository;
  private final UserMapper userMapper;

  public UserServiceImpl (UserRepository userRepository,
                        RoleRepository roleRepository,
                        LoanRepository loanRepository,
                        UserMapper userMapper) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.loanRepository = loanRepository;
    this.userMapper = userMapper;
  }

  @Override
  public PagedResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<User> users = userRepository.findAll(pageable);

    return getPagedResponse(users);
  }

  private PagedResponse<UserResponse> getPagedResponse(Page<User> users) {
    List<UserResponse> content = users.getContent().stream()
            .map(user -> {
              long activeLoans = loanRepository.countActiveLoansForUser(user.getId());
              return userMapper.toResponse(user, (int) activeLoans);
            })
            .collect(Collectors.toList());

    return new PagedResponse<>(
            content,
            users.getNumber(),
            users.getSize(),
            users.getTotalElements(),
            users.getTotalPages(),
            users.isLast());
  }

  @Override
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    long activeLoans = loanRepository.countActiveLoansForUser(id);
    return userMapper.toResponse(user, (int) activeLoans);
  }

  @Override
  public UserResponse getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

    long activeLoans = loanRepository.countActiveLoansForUser(user.getId());
    return userMapper.toResponse(user, (int) activeLoans);
  }

  @Override
  @Transactional
  public UserResponse updateUser(Long id, UserRequest userRequest) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    //Checkeamos email existte
    if (!user.getEmail().equals(userRequest.getEmail()) &&
            userRepository.existsByEmail(userRequest.getEmail())) {
      throw new RuntimeException("Email " + userRequest.getEmail() + "is already in use");
    }

    //Processs user ROles
    Set<Role> roles = new HashSet<>();
    userRequest.getRoles().forEach(rolename -> {
      Role role = roleRepository.findByName(rolename)
              .orElseThrow(() -> new RuntimeException("Role not found with name: " + rolename));
      roles.add(role);
    });

    //Update user
    userMapper.updateEntity(user, userRequest, roles);
    user = userRepository.save(user);

    long activeLoans = loanRepository.countActiveLoansForUser(id);
    return userMapper.toResponse(user, (int) activeLoans);
  }

  @Override
  @Transactional
  public void deleteUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    // Check if user has active loans
    long activeLoans = loanRepository.countActiveLoansForUser(id);
    if (activeLoans > 0) {
      throw new RuntimeException("User cannot be deleted while having active loans");
    }

    userRepository.delete(user);
  }

  @Override
  public PagedResponse<UserResponse> searchUsers(String query, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    Page<User> users = userRepository.search(query, pageable);

    return getPagedResponse(users);
  }

  @Override
  @Transactional
  public void activateUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    user.setMembershipStatus(true);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void desactivateUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    user.setMembershipStatus(false);
    userRepository.save(user);

  }

  @Override
  public long countActiveMembers() {
    return userRepository.countActiveMembers();
  }
}
