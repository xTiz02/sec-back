package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;

public record DateGuardUnityAssignmentDto(
    Long id,
    Long dayOfMonthId,
    DayOfMonth dayOfMonth,
    Long guardUnityScheduleAssignmentId,
    GuardUnityScheduleAssignmentDto guardUnityScheduleAssignment,
    Long turnAndHourId,
    TurnAndHourDto turnAndHour,
    DayOfWeek dayOfWeek,
    Integer numDay,
    LocalDate date,
    LocalDate toDate,
    ScheduleAssignmentType scheduleAssignmentType,
    Boolean hasVacation,
    Boolean hasExceptions
) {
  public DateGuardUnityAssignmentDto(DateGuardUnityAssignment dateGuardUnityAssignment){
    this(
        dateGuardUnityAssignment.getId(),
        dateGuardUnityAssignment.getDayOfMonthId(),
        dateGuardUnityAssignment.getDayOfMonth(),
        dateGuardUnityAssignment.getGuardUnityScheduleAssignmentId(),
        null,
        dateGuardUnityAssignment.getTurnAndHourId(),
        dateGuardUnityAssignment.getTurnAndHour() != null ? new TurnAndHourDto(dateGuardUnityAssignment.getTurnAndHour()) : null,
        dateGuardUnityAssignment.getDayOfWeek(),
        dateGuardUnityAssignment.getNumDay(),
        dateGuardUnityAssignment.getDate() != null ? dateGuardUnityAssignment.getDate() : null,
        dateGuardUnityAssignment.getToDate() != null ? dateGuardUnityAssignment.getToDate() : null,
        dateGuardUnityAssignment.getScheduleAssignmentType(),
        dateGuardUnityAssignment.isHasVacation(),
        dateGuardUnityAssignment.isHasExceptions()
    );
  }
}
