package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.SecurityProfile;
import java.time.LocalDateTime;

public record SecurityProfileDto (
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){
  public SecurityProfileDto (SecurityProfile securityProfile) {
    this(securityProfile.getId(), securityProfile.getName(), securityProfile.getDescription(),
        securityProfile.getCreatedAt(), securityProfile.getUpdatedAt());
  }
}
