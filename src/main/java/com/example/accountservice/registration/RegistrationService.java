package com.example.accountservice.registration;

import com.example.accountservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegistrationResponse register(RegistrationRequest request) {
        return new RegistrationResponse(request.name(), request.lastname(), request.email());
    }
}
