package com.emersondev.security;

import com.emersondev.domain.entity.Role;
import com.emersondev.domain.entity.User;
import com.emersondev.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    logger.debug("Cargando usuario por email: {}", email);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

    if (!user.getMembershipStatus()) {
      logger.warn("Usuario deshabilitado: {}", email);
      throw new UsernameNotFoundException("User account is disabled");
    }

    // Forzar inicialización de la colección antes de iterar
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    try {
      // Usar una nueva colección para evitar problemas de concurrencia
      Set<Role> roles = new HashSet<>(user.getRoles());
      authorities = roles.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
              .collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("Error al procesar roles: {}", e.getMessage(), e);
    }

    logger.debug("Usuario autenticado con {} roles", authorities.size());

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities
    );
  }
}