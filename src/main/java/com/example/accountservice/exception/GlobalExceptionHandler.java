package com.example.accountservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    final Map<String, List<String>> errorMap = new HashMap<>();
    ex.getFieldErrors()
        .forEach(fieldError -> {
          var list = errorMap.getOrDefault(fieldError.getField(), new ArrayList<>());
          list.add(fieldError.getDefaultMessage());
          errorMap.put(fieldError.getField(), list);
        });

    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
    var errorDetails = new ErrorDetails(ex.getMessage(), LocalDateTime.now(), request.getRequestURI());
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({DisabledException.class, LockedException.class})
  public ResponseEntity<ErrorDetails> handleDisabledException(DisabledException ex, HttpServletRequest request) {
    var errorDetails = new ErrorDetails(ex.getMessage(), LocalDateTime.now(), request.getRequestURI());
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }


  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorDetails> handleAuthException(AuthenticationException e,
                                                  HttpServletRequest request) {

    var details = new ErrorDetails(e.getMessage(), LocalDateTime.now(), request.getRequestURI());
    return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException e,
                                                  HttpServletRequest request) {

    var details = new ErrorDetails(e.getMessage(), LocalDateTime.now(), request.getRequestURI());
    return new ResponseEntity<>(details, HttpStatus.FORBIDDEN);
  }

}

