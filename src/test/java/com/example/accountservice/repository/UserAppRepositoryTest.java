package com.example.accountservice.repository;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.domain.SecurityUser;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;


@DataJpaTest
@TestPropertySources(
    @TestPropertySource(value = "classpath:application.yaml")
)
@Disabled
class UserSecurityRepositoryTest {

  @Autowired
  private UserSecurityRepository userSecurityRepository;

  @Autowired
  private UserAppRepository userAppRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Test
  void test() {


    AppUser appUser = new AppUser();
    appUser.setEmail("aaaa@aaa.com");
    appUser.setName("1234");
    appUser.setLastname("1234");
    appUser =  userAppRepository.saveAndFlush(appUser);

    SecurityUser securityUser = new SecurityUser();
    securityUser.setAppUser(appUser);
    securityUser.setPassword("1234");
    securityUser.setEnabled(true);
    securityUser.addRole(roleRepository.findByName("ROLE_USER"));
    userSecurityRepository.saveAndFlush(securityUser);

    BDDAssertions.assertThat(userSecurityRepository.findRoleNamesByAppUser(appUser).size())
        .isEqualTo(1);
  }

}