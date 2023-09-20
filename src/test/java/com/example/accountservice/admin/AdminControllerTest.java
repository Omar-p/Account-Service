package com.example.accountservice.admin;

import com.example.accountservice.authentication.AuthenticationConfig;
import com.example.accountservice.config.SecurityConfig;
import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.exception.security.DelegatedAuthEntryPoint;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import({SecurityConfig.class, AuthenticationConfig.class, DelegatedAuthEntryPoint.class})
@AutoConfigureRestDocs
class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserDetailsService userDetailsService;

  public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor JWT_REQUEST_POST_PROCESSOR_ADMIN = SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> {
    jwt.claims(claims -> {
      claims.put("scope", "ROLE_ADMINISTRATOR");
    }).subject("JohnDoe@acme.com");
  });

  public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor JWT_REQUEST_POST_PROCESSOR_USER = SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> {
    jwt.claims(claims -> {
      claims.put("scope", "ROLE_USER");
    }).subject("JohnDoe@acme.com");
  });

  @Test
  void givenNonExistingUser_whenDeleteUser_thenReturnsNotFound() throws Exception {
    final String email = "john@acme.com";
    BDDMockito.given(userService.deleteUser(email))
        .willThrow(new UserNotFoundException("User not found!"));

    mockMvc
        .perform(delete("/api/admin/user/{email}", email).with(JWT_REQUEST_POST_PROCESSOR_ADMIN))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("User not found!"))
          .andDo(MockMvcRestDocumentation.document("deleteNonExistingUser"));
  }

  @Test
  void givenExistingUser_whenDeleteUser_thenReturnsOk() throws Exception {
    final String email = "john@acme.com";
    BDDMockito.given(userService.deleteUser(email))
        .willReturn(new DeleteUserResponse(email, "Deleted successfully!"));

    mockMvc
        .perform(delete("/api/admin/user/{email}", email).with(JWT_REQUEST_POST_PROCESSOR_ADMIN))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("Deleted successfully!"))
          .andDo(MockMvcRestDocumentation.document("delete User"));
  }

  @Test
  void givenTokenWithUserRole_whenDeleteUser_thenReturnsForbidden() throws Exception {
    final String email = "john@acme.com";
    mockMvc
        .perform(delete("/api/admin/user/{email}", email).with(JWT_REQUEST_POST_PROCESSOR_USER))
        .andExpect(status().isForbidden())
        .andDo(MockMvcRestDocumentation.document("deleteUserWithoutPermission"));
  }
}