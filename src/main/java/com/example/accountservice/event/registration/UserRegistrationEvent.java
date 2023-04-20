package com.example.accountservice.event.registration;

import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent {

  private String password;


  public UserRegisteredEvent(Object source, String password) {
    super(source);
    this.password = password;
  }


}
