package com.example.accountservice.exception;

import java.time.LocalDateTime;

public record ErrorDetails(String message, LocalDateTime timestamp, String path) {
}
