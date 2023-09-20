package com.example.accountservice.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
class LoginController {

  private final AccessTokenService accessTokenService;
  private final AuthenticationManager authenticationManager;

  LoginController(AccessTokenService accessTokenService, AuthenticationManager authenticationManager) {
    this.accessTokenService = accessTokenService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping
  ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
    final Authentication authentication = authenticationManager.authenticate(authenticationToken);
    var accessToken = accessTokenService.generateAccessToken(authentication);

    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .build();
  }
}
