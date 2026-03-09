package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardAssignment;
import java.time.LocalDateTime;

public record GuardAssignmentDto(
    Long id,
    Long guardId,
    GuardDto guard,
    ExternalGuardDto externalGuard,
    Long employeeUnitAssignmentId,
    Boolean active,
    LocalDateTime createdAt
) {
  public GuardAssignmentDto(GuardAssignment guardAssignment) {
    this(
        guardAssignment.getId(),
        guardAssignment.getGuardId(),
        guardAssignment.getGuard() != null ? new GuardDto(guardAssignment.getGuard()) : null,
        guardAssignment.getExternalGuard() != null ? new ExternalGuardDto(guardAssignment.getExternalGuard()) : null,
        guardAssignment.getEmployeeUnitAssignmentId(),
        guardAssignment.isActive(),
        guardAssignment.getCreatedAt()
    );
  }
}
