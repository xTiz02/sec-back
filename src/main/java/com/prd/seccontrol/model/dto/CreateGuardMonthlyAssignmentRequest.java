package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;

public record CreateGuardMonthlyAssignmentRequest(
    Long guardId,
    Long contractUnityId,
    Long scheduleMonthlyId,
    GuardType guardType
) {

}
