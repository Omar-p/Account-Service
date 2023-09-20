package com.example.accountservice;

import com.example.accountservice.config.RsaKeyProperties;
import com.example.accountservice.domain.Group;
import com.example.accountservice.domain.Role;
import com.example.accountservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AccountServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountServiceApplication.class, args);
  }

}
