package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.types.GuardType;

public record GuardDto(
    Long id,
    Long employeeId,
    String code,
    EmployeeDto employee,
    String licenseNumber,
    GuardType guardType,
    String photoUrl
) {
  public GuardDto(Guard guard) {
    this(
        guard.getId(),
        guard.getEmployeeId(),
        guard.getCode(),
        guard.getEmployee() != null ? new EmployeeDto(guard.getEmployee()) : null,
        guard.getLicenseNumber(),
        guard.getGuardType(),
        guard.getPhotoUrl()
    );
  }
}
