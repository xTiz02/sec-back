package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.ScheduleExceptionType;

public record CreateScheduleExceptionRequest(
    Long guardUnityScheduleAssignmentId,
    String motive,
    String description,
    Long dateGuardUnityAssignmentId,
    Long scheduleMonthlyId,
    ScheduleExceptionType scheduleExceptionType
) {

}
