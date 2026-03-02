package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.EmployeeUnitAssignment;
import com.prd.seccontrol.model.types.ZoneType;

public record EmployeeUnitAssignmentDto(
    Long id,
    Long employeeAssignmentMonthlyId,
    Long unityId,
    UnityDto unity,
    ZoneType zoneType
) {
 public EmployeeUnitAssignmentDto(EmployeeUnitAssignment entity) {
        this(
            entity.getId(),
            entity.getEmployeeAssignmentMonthlyId(),
            entity.getUnity().getId(),
            new UnityDto(entity.getUnity()),
            entity.getZoneType()
        );
    }
}
