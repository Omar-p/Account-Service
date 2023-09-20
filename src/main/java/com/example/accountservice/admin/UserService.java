package com.example.accountservice.admin;

import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.repository.UserAppRepository;
import com.example.accountservice.repository.UserSecurityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class UserService {

  private final UserAppRepository userAppRepository;
  private final UserSecurityRepository userSecurityRepository;

  UserService(UserAppRepository userAppRepository, UserSecurityRepository userSecurityRepository) {
    this.userAppRepository = userAppRepository;
    this.userSecurityRepository = userSecurityRepository;
  }

  List<UserInfo> getAllUsers() {
    var users = userAppRepository.findAll();
    var userInfoList = new ArrayList<UserInfo>();
    for (var user : users) {
      userInfoList.add(new UserInfo(
          user.getEmail(),
          user.getName(),
          user.getLastname(),
          userSecurityRepository.findRoleNamesByAppUser(user)));
    }
    return userInfoList;
  }

  DeleteUserResponse deleteUser(String userEmail) {
    userAppRepository.findByEmail(userEmail)
        .ifPresentOrElse(userSecurityRepository::deleteByAppUser, () -> {
              throw new UserNotFoundException("User not found!");
        });


    return new DeleteUserResponse(userEmail, "Deleted successfully!");
  }

  public RoleOperationResponse applyRoleOperation(RoleOperationRequest request) {
    return null;
  }
}
