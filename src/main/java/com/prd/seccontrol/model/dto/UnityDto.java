package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.Unity;
import java.time.LocalDateTime;

public record UnityDto(
    Long id,
    Long clientId,
    String clientName,
    Boolean active,
    String code,
    String name,
    String description,
    String direction,
    Double latitude,
    Double longitude,
    Double rangeCoverage,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
  public UnityDto(Unity unity) {
    this(
        unity.getId(),
        unity.getClient() != null ? unity.getClient().getId() : null,
        unity.getClient() != null ? unity.getClient().getName() : null,
        unity.isActive(),
        unity.getCode(),
        unity.getName(),
        unity.getDescription(),
        unity.getDirection(),
        unity.getLatitude(),
        unity.getLongitude(),
        unity.getRangeCoverage(),
        unity.getCreatedAt(),
        unity.getUpdatedAt()
    );
  }
}
