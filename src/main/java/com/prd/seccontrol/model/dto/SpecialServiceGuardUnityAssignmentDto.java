package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;

public record SpecialServiceGuardUnityAssignmentDto(
    Long id,
    GuardType guardType,
    GuardAssignmentDto guardAssignment
) {

}
