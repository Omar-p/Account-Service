package com.example.accountservice.authentication;

import com.example.accountservice.config.RsaKeyProperties;
import com.example.accountservice.repository.UserAppRepository;
import com.example.accountservice.repository.UserSecurityRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public
class AuthenticationConfig {

  private final RsaKeyProperties rsaKeys;

  public AuthenticationConfig(RsaKeyProperties rsaKeys) {
    this.rsaKeys = rsaKeys;
  }

  @Bean
  @DependsOn({"userDetailsServiceByEmail", "passwordEncoder"})
  AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder encoder) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder);
    return new ProviderManager(authProvider);
  }

  @Bean
  UserDetailsService userDetailsServiceByEmail(UserAppRepository userAppRepository, UserSecurityRepository userSecurityRepository) {
    return email -> {
      var user = userAppRepository.findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("email or password is wrong"));
      return userSecurityRepository.findByAppUser(user);
    };
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
  }

  @Bean
  JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }
}
