package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.AssignProfilesRequest;
import com.prd.seccontrol.model.dto.CreateUserRequest;
import com.prd.seccontrol.model.dto.UpdateUserRequest;
import com.prd.seccontrol.model.dto.UserPassword;
import com.prd.seccontrol.model.entity.SecurityProfile;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.model.entity.ViewAuthorization;
import com.prd.seccontrol.repository.SecurityProfileRepository;
import com.prd.seccontrol.repository.UserRepository;
import com.prd.seccontrol.repository.ViewAuthorizationRepository;
import com.prd.seccontrol.service.impl.UserService;
import com.prd.seccontrol.util.SEConstants;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ViewAuthorizationRepository viewAuthorizationRepository;

  @Autowired
  private SecurityProfileRepository securityProfileRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users/all")
  public @ResponseBody Page<User> findAll(Pageable pageable) {

    return userRepository.findAll(pageable);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users/{id}")
  public @ResponseBody User findById(@PathVariable(value = "id") Long id) throws Exception {
    return userRepository.findById(id).orElseThrow(() -> new Exception("User not found with id: " + id));
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users/self")
  public @ResponseBody User findSelf(Principal principal) throws Exception {

    Set<Long> securityProfileIdSet = new HashSet<>();

    User user = userService.getActiveUser(principal);

    // If a user has no security profiles set, throw exception.
    if (user.getSecurityProfileSet().isEmpty()) {
      throw new Exception();
    }

    // Get security profile IDs.
    for (SecurityProfile securityProfile : user.getSecurityProfileSet()) {
      Long id = securityProfile.getId();
      if (id != null) {
        securityProfileIdSet.add(id);
      }
    }

    // Set transient viewAuthorizations in user.
    List<ViewAuthorization> viewAuthorizations =
        viewAuthorizationRepository.findViewBySecurityProfileId(securityProfileIdSet);

    user.setViewAuthorizations(viewAuthorizations);

    return user;
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users")
  public @ResponseBody User create(@RequestBody CreateUserRequest user) {
    return userService.createUser(user);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users/{id}")
  public @ResponseBody User edit(@PathVariable(value = "id") Long id, @RequestBody UpdateUserRequest request) throws Exception {
    return userService.updateUser(id, request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/users/self/{id}")
  public @ResponseBody User editSelfPassword(@PathVariable(value = "id") Long id,
      @RequestBody UserPassword password, Principal principal) throws Exception {

    User user = userService.getActiveUser(principal);
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    if (user.getId().equals(id)) {
      if (password.newPassword().equals(password.confirmPassword())) {
        if (encoder.matches(password.currentPassword(), user.getPassword())) {
          user.setPassword(encoder.encode(password.newPassword()));
          userRepository.save(user);

        } else {
          throw new RuntimeException("Current password is incorrect.");
        }

      } else {
        throw new RuntimeException("New password and confirm password do not match.");
      }
    } else {
      throw new Exception("User can only edit their own password.");
    }

    return user;
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/user/{id}")
  public @ResponseBody Long delete(@PathVariable(value = "id") Long id) {
    userRepository.deleteById(id);
    return id;
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/user/{id}/profiles")
  public @ResponseBody User assignSecurityProfiles(@PathVariable(value = "id") Long id,
      @RequestBody AssignProfilesRequest request) throws Exception {
    User user = userRepository.findById(id).orElseThrow(() -> new Exception("User not found with id: " + id));

    Set<SecurityProfile> securityProfiles = securityProfileRepository.findByIdIn(request.profileIds());

    user.setSecurityProfileSet(securityProfiles);
    return userRepository.save(user);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/user/{id}/status")
  public @ResponseBody User updateUserStatus(@PathVariable(value = "id") Long id,
      @RequestBody Boolean enabled) throws Exception {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new Exception("User not found with id: " + id));
    user.setEnabled(enabled);
    return userRepository.save(user);
  }


}
