package com.prd.seccontrol.model.dto;

import java.time.LocalDate;

public record SpecialServiceDayAssignmentDto(
    Long id,
    LocalDate date,
    SpecialServiceGuardUnityAssignmentDto guardUnityScheduleAssignment,
    TurnAndHourDto turnAndHour
) {

}
