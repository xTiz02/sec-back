package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleExceptionType;

public record CreateSpecialServiceExceptionRequest(
    Long dateGuardUnityAssignmentId,
    Long guardId,
    Long externalGuardId,
    GuardType guardType,
    ScheduleExceptionType scheduleExceptionType,
    String motive,
    Long scheduleId,
    String description
) {

}
