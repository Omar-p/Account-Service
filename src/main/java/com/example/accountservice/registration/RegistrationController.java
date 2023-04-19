package com.example.accountservice.registration;

import com.example.accountservice.exception.BreachedPasswordException;
import com.example.accountservice.exception.ErrorDetails;
import com.example.accountservice.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class RegistrationController {

  private final RegistrationService registrationService;


  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @PostMapping("/api/auth/signup")
  public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
    return ResponseEntity.ok(registrationService.register(request));
  }


  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorDetails> handleRegistrationWithAnotherEmail(Exception ex, HttpServletRequest request) {
    var errorDetails = new ErrorDetails(
        ex.getMessage(),
        LocalDateTime.now(),
        request.getRequestURI()
    );

    return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BreachedPasswordException.class)
  public ResponseEntity<ErrorDetails> handleRegistrationWithBreachedPassword(Exception ex, HttpServletRequest request) {
    var errorDetails = new ErrorDetails(
        ex.getMessage(),
        LocalDateTime.now(),
        request.getRequestURI()
    );

    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}
