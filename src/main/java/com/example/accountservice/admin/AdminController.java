package com.example.accountservice.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
class AdminController {

  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMINISTRATOR')")
  public ResponseEntity<List<UserInfo>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }


  @PutMapping("/role")
  public ResponseEntity<?> changeRole(@RequestBody RoleOperationRequest request) {
    return ResponseEntity
        .ok(userService.applyRoleOperation(request));
  }

  @DeleteMapping("/{userEmail}")
  @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMINISTRATOR')")
  public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable("userEmail") String userEmail) {

    return ResponseEntity.ok().body(userService.deleteUser(userEmail));
  }
}
