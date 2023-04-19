package com.example.accountservice.registration;

import com.example.accountservice.exception.UserAlreadyExistsException;
import com.example.accountservice.password.PasswordValidator;
import com.example.accountservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

  private final UserRepository userRepository;
  private final PasswordValidator passwordValidator;
  private final PasswordEncoder passwordEncoder;

  public RegistrationService(UserRepository userRepository, PasswordValidator passwordValidator, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordValidator = passwordValidator;
    this.passwordEncoder = passwordEncoder;
  }

  public RegistrationResponse register(RegistrationRequest request) {
    this.checkIfEmailExist(request.email());
    passwordValidator.checkIfPasswordBreached(request.password());
    persistUser(request);
    return new RegistrationResponse(request.name(), request.lastname(), request.email());
  }

  private void persistUser(RegistrationRequest request) {
  }

  private void checkIfEmailExist(String email) {
    if (userRepository.existsByEmail(email))
      throw new UserAlreadyExistsException("User with email " + email + " already exists");
  }
}
