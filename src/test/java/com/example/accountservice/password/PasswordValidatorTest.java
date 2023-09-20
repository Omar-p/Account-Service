package com.example.accountservice.password;

import com.example.accountservice.exception.BreachedPasswordException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Disabled
class PasswordValidatorTest {

  @Autowired
  PasswordValidator passwordValidator;

  @Test
  void givenBreachedPasswordItShouldThrowException() {
    BDDAssertions.assertThatThrownBy(() -> passwordValidator.checkIfPasswordBreached("PasswordForApril"))
        .isInstanceOf(BreachedPasswordException.class)
        .hasMessage("Password is breached");
  }

  @Test
  void givenNotBreachedPasswordItShouldNotThrowException() {
    BDDAssertions.assertThatNoException()
        .isThrownBy(() -> passwordValidator.checkIfPasswordBreached("PasswordForMay1"));
  }

}