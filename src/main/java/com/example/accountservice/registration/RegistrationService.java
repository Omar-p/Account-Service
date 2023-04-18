package com.example.accountservice.registration;

import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    public RegistrationResponse register(RegistrationRequest request) {
        return new RegistrationResponse(request.name(), request.lastname(), request.email());
    }
}
