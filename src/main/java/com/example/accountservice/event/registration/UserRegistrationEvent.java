package com.example.accountservice.event.registration;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

  private final String password;


  public UserRegistrationEvent(Object source, String password) {
    super(source);
    this.password = password;
  }

  public String getPassword() {
    return password;
  }


}
