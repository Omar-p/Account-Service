package com.example.accountservice.repository;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.domain.Role;
import com.example.accountservice.domain.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSecurityRepository extends JpaRepository<SecurityUser, Long> {


  @Query("select r.name from SecurityUser u join u.roles r where u.appUser = ?1")
  List<String> findRoleNamesByAppUser(AppUser appUser);

  SecurityUser findByAppUser(AppUser appUser);

  void deleteByAppUser(AppUser appUser);

}
