package com.example.accountservice.registration;

import com.example.accountservice.exception.UserAlreadyExistsException;
import com.example.accountservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.BDDMockito.*;

@MockitoSettings
class RegistrationServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegistrationService registrationService;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    RegistrationRequest request =
            new RegistrationRequest("John", "Doe", "john@acme.com", "secretsecretsecret");
    @Test
    void givenRegistrationRequestWithAlreadyUsedEmailItShouldReturn409() {
        given(userRepository.existsByEmail(emailCaptor.capture()))
                .willReturn(true);


        assertThatThrownBy(() -> registrationService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with email " + request.email() + " already exists");

        BDDMockito.then(userRepository).should().existsByEmail(emailCaptor.getValue());
        BDDMockito.then(userRepository).shouldHaveNoMoreInteractions();
    }



}