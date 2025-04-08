package com.emersondev.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "users")
public class Role extends Audit{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(length = 500)
  private String description;

  @ManyToMany(mappedBy = "roles")
  @ToString.Exclude //También excluir de toString para evitar ciclos infinitos
  private Set<User> users = new HashSet<>();
}
