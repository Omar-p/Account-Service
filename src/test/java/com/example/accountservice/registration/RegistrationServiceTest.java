package com.example.accountservice.registration;

import com.example.accountservice.exception.UserAlreadyExistsException;
import com.example.accountservice.repository.UserAppRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.BDDAssertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class RegistrationServiceTest {

  @Mock
  UserAppRepository userAppRepository;

  @InjectMocks
  RegistrationService registrationService;

  @Captor
  private ArgumentCaptor<String> emailCaptor;

  RegistrationRequest request =
      new RegistrationRequest("John", "Doe", "john@acme.com", "secretsecretsecret");

  @Test
  void givenRegistrationRequestWithAlreadyUsedEmailItShouldThrowUserAlreadyExistsException() {
    given(userAppRepository.existsByEmail(emailCaptor.capture()))
        .willReturn(true);


    assertThatThrownBy(() -> registrationService.register(request))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessage("User with email " + request.email() + " already exists");

    BDDMockito.then(userAppRepository).should().existsByEmail(emailCaptor.getValue());
    BDDMockito.then(userAppRepository).shouldHaveNoMoreInteractions();
  }


}