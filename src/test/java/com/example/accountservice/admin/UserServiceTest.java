package com.example.accountservice.admin;

import com.example.accountservice.domain.AppUser;
import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.repository.UserAppRepository;
import com.example.accountservice.repository.UserSecurityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
class UserServiceTest {

  @Mock
  private UserAppRepository userAppRepository;

  @Mock
  private UserSecurityRepository userSecurityRepository;

  @InjectMocks
  private UserService userService;


  @Test
  void givenUserEmail_whenEmailExists_thenDeleteUser() {
    String userEmail = "john@acme.com";
    BDDMockito.given(userAppRepository.findByEmail(userEmail))
        .willReturn(Optional.of(new AppUser()));

    var actual = userService.deleteUser(userEmail);
    var expected = new DeleteUserResponse(userEmail, "Deleted successfully!");

    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);

    verify(userSecurityRepository, times(1)).deleteByAppUser(any(AppUser.class));
  }

  @Test
  void givenUserEmail_whenEmailDoesNotExist_thenThrowUserNotFoundException() {
    String userEmail = "john@acme.com";
    BDDMockito.given(userAppRepository.findByEmail(userEmail))
        .willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userEmail));

    verify(userSecurityRepository, times(0)).deleteByAppUser(any(AppUser.class));

    verifyNoInteractions(userSecurityRepository);
  }
}