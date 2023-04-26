package com.example.accountservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
  name = "role",
  uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "role_name_key")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
  @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "group_name")
  private Group group;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "role_permission",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id"),
    foreignKey = @ForeignKey(name = "role_permission_role_id_fkey"),
    inverseForeignKey = @ForeignKey(name = "role_permission_permission_id_fkey")
  )
  private Set<Permission> permissions;

  public Role(String name, Group group) {
    this.name = name;
    this.group = group;
  }

  public Set<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(name));
    permissions
        .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
    return authorities;
  }
}
