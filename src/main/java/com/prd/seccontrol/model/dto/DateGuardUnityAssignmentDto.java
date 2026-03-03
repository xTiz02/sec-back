package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;

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
    String date,
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
        null,
        dateGuardUnityAssignment.getDayOfWeek(),
        dateGuardUnityAssignment.getNumDay(),
        dateGuardUnityAssignment.getDate() != null ? dateGuardUnityAssignment.getDate().toString() : null,
        dateGuardUnityAssignment.getScheduleAssignmentType(),
        dateGuardUnityAssignment.isHasVacation(),
        dateGuardUnityAssignment.isHasExceptions()
    );
  }
}
