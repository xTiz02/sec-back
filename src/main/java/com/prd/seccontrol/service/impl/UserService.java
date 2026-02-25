package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateUserRequest;
import com.prd.seccontrol.model.dto.UpdateUserRequest;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.repository.UserRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User getActiveUser(Principal principal) {
    return userRepository.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException(
        "Active User not found")
    );
  }

  public User createUser(CreateUserRequest createUserRequest) {
    User user = new User();
    user.setUsername(createUserRequest.username());
    user.setPassword(passwordEncoder.encode(createUserRequest.password()));
    user.setAccountExpired(false);
    user.setAccountLocked(false);
    user.setCredentialsExpired(false);
    user.setEnabled(createUserRequest.enabled());
    return userRepository.save(user);
  }

  public User updateUser(Long id, UpdateUserRequest request) throws Exception {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new Exception("User not found with id: " + id));
    user.setUsername(request.username());
    user.setAccountExpired(request.accountExpired());
    user.setAccountLocked(request.accountLocked());
    user.setCredentialsExpired(request.credentialsExpired());
    user.setEnabled(request.enabled());
    return userRepository.save(user);
  }
}
