package com.example.accountservice.repository;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.domain.Role;
import com.example.accountservice.domain.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSecurityRepository extends JpaRepository<SecurityUser, Long> {

  @Query("select u.roles from SecurityUser u where u.appUser = ?1")
  List<Role> findRolesByAppUser(AppUser appUser);

  SecurityUser findByAppUser(AppUser appUser);

}
