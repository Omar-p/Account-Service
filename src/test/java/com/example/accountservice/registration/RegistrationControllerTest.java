package com.example.accountservice.registration;

import com.example.accountservice.config.SecurityConfig;
import com.example.accountservice.exception.GlobalExceptionHandler;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void givenValidRegistrationRequestItShouldReturn200WithUserInfo() throws Exception {

        BDDMockito.given(registrationService.register(BDDMockito.any(RegistrationRequest.class)))
                .willReturn(new RegistrationResponse("John", "Doe", "johndoe@acme.com"));

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "name": "John",
                                   "lastname": "Doe",
                                   "email": "johndoe@acme.com",
                                   "password": "secret"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                           "name": "John",
                           "lastname": "Doe",
                           "email": "johndoe@acme.com"
                        }
                        """))
                .andDo(document("registration",
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
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(4)))
                .andDo(document("registration-invalid"));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.email[0]", is("Email must belong to acme.com domain")));
    }

}