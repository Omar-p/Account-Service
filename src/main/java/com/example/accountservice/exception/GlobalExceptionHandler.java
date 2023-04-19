package com.example.accountservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

}
