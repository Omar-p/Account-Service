package com.example.accountservice.registration;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.event.registration.UserRegistrationEvent;
import com.example.accountservice.exception.UserAlreadyExistsException;
import com.example.accountservice.password.PasswordValidator;
import com.example.accountservice.repository.UserAppRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

  private final UserAppRepository userRepository;
  private final PasswordValidator passwordValidator;
  private final PasswordEncoder passwordEncoder;

  private final ApplicationEventPublisher eventPublisher;

  public RegistrationService(UserAppRepository userAppRepository, PasswordValidator passwordValidator, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
    this.userRepository = userAppRepository;
    this.passwordValidator = passwordValidator;
    this.passwordEncoder = passwordEncoder;
    this.eventPublisher = eventPublisher;
  }

  public RegistrationResponse register(RegistrationRequest request) {
    this.checkIfEmailExist(request.email());
    passwordValidator.checkIfPasswordBreached(request.password());
    persistUser(request);
    return new RegistrationResponse(request.name(), request.lastname(), request.email());
  }

  private void persistUser(RegistrationRequest request) {
    AppUser user = new AppUser();
    user.setEmail(request.email());
    user.setName(request.name());
    user.setLastname(request.lastname());
    user = userRepository.save(user);
    eventPublisher.publishEvent(new UserRegistrationEvent(user, request.password()));
  }

  private void checkIfEmailExist(String email) {
    if (userRepository.existsByEmail(email))
      throw new UserAlreadyExistsException("User with email " + email + " already exists");
  }
}
