package com.emersondev.domain.repository;

import com.emersondev.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
  Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

  @Query("SELECT u FROM User u WHERE " +
          "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
          "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
  Page<User> search(@Param("searchTerm") String searchTerm, Pageable pageable);

  @Query("SELECT COUNT(u) FROM User u WHERE u.membershipStatus = true")
  long countActiveMembers();
}
