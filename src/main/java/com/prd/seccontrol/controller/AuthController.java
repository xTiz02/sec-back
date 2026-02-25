package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.AuthResponse;
import com.prd.seccontrol.model.dto.LoginRequest;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.repository.UserRepository;
import com.prd.seccontrol.service.impl.JwtService;
import com.prd.seccontrol.util.SEConstants;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  @PostMapping(SEConstants.PUBLIC_BASE_ENDPOINT + "/auth/login")
  public ResponseEntity<?> login(@ModelAttribute LoginRequest request) {
    String username = request.username();
    String password = request.password();
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                username,
                password
            )
        );

    User user = (User) authentication.getPrincipal();

    String token = jwtService.generateToken(user);

    userRepository.updateLastLoginTimeByUsername(user.getUsername(), new Date());

    return ResponseEntity.ok(new AuthResponse(token));
  }

}
