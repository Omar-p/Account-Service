package com.example.accountservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "security_user")
@Getter
@Setter
@NoArgsConstructor
public class SecurityUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_user_id_seq")
  @SequenceGenerator(name = "security_user_id_seq", sequenceName = "security_user_id_seq", allocationSize = 1)
  private Long id;


  private String password;

  private boolean enabled;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "app_user_id", referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "security_user_app_user_id_fkey")
  )
  private AppUser appUser;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"),
    foreignKey = @ForeignKey(name = "user_role_user_id_fkey"),
    inverseForeignKey = @ForeignKey(name = "user_role_role_id_fkey")
  )
  private Set<Role> roles = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles
        .stream()
        .map(Role::getAuthorities)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return appUser.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void addRole(Role role) {
    roles.add(role);
  }
}
