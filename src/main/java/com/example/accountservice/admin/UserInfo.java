package com.example.accountservice.admin;

import java.util.List;

public record UserInfo(String name, String lastname, String email, List<String> roles) {
  public UserInfo {
    roles = List.copyOf(roles);
  }
}
