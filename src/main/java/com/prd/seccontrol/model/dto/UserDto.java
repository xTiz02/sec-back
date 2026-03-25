package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.User;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record UserDto(
    Long id,
    String username,
    boolean enabled,
    boolean accountExpired,
    boolean accountLocked,
    boolean credentialsExpired,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Date lastLogin,
    List<SecurityProfileDto> securityProfileSet
) {
  public UserDto(User user) {
    this(
        user.getId(),
        user.getUsername(),
        user.isEnabled(),
        user.isAccountExpired(),
        user.isAccountLocked(),
        user.isCredentialsExpired(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getLastLogin(),
        user.getSecurityProfileSet().stream().map(SecurityProfileDto::new).toList()
    );
  }
}
