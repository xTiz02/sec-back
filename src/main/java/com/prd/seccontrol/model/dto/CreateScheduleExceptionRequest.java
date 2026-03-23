package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleExceptionType;

public record CreateScheduleExceptionRequest(
    Long guardId,
    Long externalGuardId,
    GuardType guardType,
    String motive,
    String description,
    Long dateGuardUnityAssignmentId,
    Long scheduleMonthlyId,
    ScheduleExceptionType scheduleExceptionType
) {

}
