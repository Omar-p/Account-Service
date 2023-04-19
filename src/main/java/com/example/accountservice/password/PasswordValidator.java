package com.example.accountservice.password;

import com.example.accountservice.exception.BreachedPasswordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PasswordValidator {

  @Value("#{'${app.breached.passwords}'}")
  private Set<String> breachedPasswords;

  public void checkIfPasswordBreached(String password) {
    if (breachedPasswords.contains(password)) {
      throw new BreachedPasswordException("Password is breached");
    }
  }

}
