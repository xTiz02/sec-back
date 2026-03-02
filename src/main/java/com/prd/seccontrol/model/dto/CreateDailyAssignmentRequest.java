package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;

public record CreateDailyAssignmentRequest(
    LocalDate date,
    Long guardUnityScheduleAssignmentId,
    Long turnAndHourId,
    ScheduleAssignmentType scheduleAssignmentType
) {

}
