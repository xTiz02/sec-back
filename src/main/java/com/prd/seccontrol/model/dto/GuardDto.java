package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.types.GuardType;

public record GuardDto(
    Long id,
    Long employeeId,
    EmployeeDto employee,
    String licenseNumber,
    GuardType guardType,
    String photoUrl
) {
  public GuardDto(Guard guard) {
    this(
        guard.getId(),
        guard.getEmployeeId(),
        guard.getEmployee() != null ? new EmployeeDto(guard.getEmployee()) : null,
        guard.getLicenseNumber(),
        guard.getGuardType(),
        guard.getPhotoUrl()
    );
  }
}
