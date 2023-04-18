package com.example.accountservice.registration;

public record RegistrationRequest(String name, String lastName, String email, String password) {
}
