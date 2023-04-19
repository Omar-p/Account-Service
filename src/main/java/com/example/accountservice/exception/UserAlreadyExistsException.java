package com.example.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

        public UserAlreadyExistsException(String message) {
            super(message);
        }
}
