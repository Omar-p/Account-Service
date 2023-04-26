package com.example.accountservice.repository;

import com.example.accountservice.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<AppUser, Long> {

  boolean existsByEmail(String email);

  Optional<AppUser> findByEmail(String email);
}
