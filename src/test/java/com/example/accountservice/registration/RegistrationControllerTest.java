package com.example.accountservice.registration;

import com.example.accountservice.config.SecurityConfig;
import com.example.accountservice.exception.BreachedPasswordException;
import com.example.accountservice.exception.GlobalExceptionHandler;
import com.example.accountservice.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@MockitoSettings
class RegistrationControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    RegistrationService registrationService;

    private final String validRequest = """
                                {
                                   "name": "John",
                                   "lastname": "Doe",
                                   "email": "johndoe@acme.com",
                                   "password": "secretsecretsecret"
                                }
                                """;

    @Test
    void givenValidRegistrationRequestItShouldReturn200WithUserInfo() throws Exception {

        BDDMockito.given(registrationService.register(BDDMockito.any(RegistrationRequest.class)))
                .willReturn(new RegistrationResponse("John", "Doe", "johndoe@acme.com"));

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "name": "John",
                           "lastname": "Doe",
                           "email": "johndoe@acme.com"
                        }
                        """))
                .andDo(document("registration", preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("String value, not empty"),
                                        fieldWithPath("lastname").description("String value, not empty"),
                                        fieldWithPath("email").description("String value, not empty"),
                                        fieldWithPath("password").description("String value, not empty")
                                )
                        )
                );
    }

    @Test
    void givenInvalidRegistrationRequestItShouldReturn400() throws Exception {

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "name": "",
                                   "email": "",
                                   "password": ""
                                }"""
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andDo(document("registration-invalid", preprocessResponse(prettyPrint())));
    }

    @Test
    void givenEmailNotBelongingToAcmeDomainItShouldReturn400() throws Exception {

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "name": "John",
                               "lastname": "Doe",
                               "email": "aa@aaa.com",
                               "password": "secret"
                            }"""
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email[0]", is("Email must belong to acme.com domain")))
                .andDo(document("registration-with-email-from-non-acme-domain", preprocessResponse(prettyPrint())));
    }

    @Test
    void givenEmailBelongToAcmeDomainButNotValidItShouldReturn400() throws Exception {

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                               "name": "John",
                               "lastname": "Doe",
                               "email": "@acme.com",
                               "password": "secret"
                           }"""
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email[0]", is("must be a well-formed email address")))
                .andDo(document("registration-with-email-from-acme-domain-but-not-valid", preprocessResponse(prettyPrint())));
        }

    @Test
    void givenRegistrationRequestWithAlreadyExistingEmailShouldReturn409() throws Exception {

        String email = "johndoe@acme.com";
        BDDMockito.given(registrationService.register(BDDMockito.any(RegistrationRequest.class)))
                .willThrow(new UserAlreadyExistsException("User with email " + email + " already exists"));

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("User with email " + email + " already exists")))
                .andDo(document("registration-with-already-existing-email", preprocessResponse(prettyPrint())));
    }

    @Test
    void givenRegistrationRequestWithBreachedPasswordShouldReturn400() throws Exception {

        BDDMockito.given(registrationService.register(BDDMockito.any(RegistrationRequest.class)))
                .willThrow(new BreachedPasswordException("Password is breached"));

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Password is breached")))
                .andDo(document("registration-with-breached-password", preprocessResponse(prettyPrint())));
    }

}