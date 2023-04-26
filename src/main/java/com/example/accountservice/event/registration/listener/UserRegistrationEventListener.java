package com.example.accountservice.event.registration.listener;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.domain.Role;
import com.example.accountservice.domain.SecurityUser;
import com.example.accountservice.event.registration.UserRegistrationEvent;
import com.example.accountservice.repository.RoleRepository;
import com.example.accountservice.repository.UserSecurityRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

  private final RoleRepository roleRepository;
  private final UserSecurityRepository userSecurityRepository;

  private final PasswordEncoder passwordEncoder;

  public UserRegistrationEventListener(RoleRepository roleRepository, UserSecurityRepository userSecurityRepository, PasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userSecurityRepository = userSecurityRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onApplicationEvent(UserRegistrationEvent event) {
    AppUser user = (AppUser) event.getSource();
    SecurityUser securityUser = new SecurityUser();
    securityUser.setAppUser(user);
    securityUser.addRole(getRole());
    securityUser.setPassword(passwordEncoder.encode(event.getPassword()));
    userSecurityRepository.save(securityUser);
  }

  Role getRole() {
    if (userSecurityRepository.count() == 0) {
      return roleRepository.findByName("ROLE_ADMINISTRATOR");
    }
    return roleRepository.findByName("ROLE_USER");
  }
}
