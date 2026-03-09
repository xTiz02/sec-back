package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.model.types.ScheduleExceptionType;

public record ScheduleExceptionDto(
    Long id,
    Long guardUnityScheduleAssignmentId,
    String motive,
    String description,
    Long dateGuardUnityAssignmentId,
    Long scheduleMonthlyId,
    Integer orderIndex,
    ScheduleExceptionType scheduleExceptionType
) {
  public ScheduleExceptionDto(ScheduleException scheduleException) {
    this(scheduleException.getId(), scheduleException.getGuardUnityScheduleAssignmentId(),
        scheduleException.getMotive(), scheduleException.getDescription(),
        scheduleException.getDateGuardUnityAssignmentId(), scheduleException.getScheduleMonthlyId(),
        scheduleException.getOrderIndex(), scheduleException.getScheduleExceptionType());
  }
}
