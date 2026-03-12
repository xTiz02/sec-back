package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.model.types.ScheduleExceptionType;

public record ScheduleExceptionDto(
    Long id,
    Long guardUnityScheduleAssignmentId,
    GuardUnityScheduleAssignmentDto guardUnityScheduleAssignment,
    String motive,
    String description,
    Long dateGuardUnityAssignmentId,
    Long scheduleMonthlyId,
    Integer orderIndex,
    ScheduleExceptionType scheduleExceptionType
) {
  public ScheduleExceptionDto(ScheduleException scheduleException, boolean includeGuardUnityScheduleAssignment) {
    this(scheduleException.getId(), scheduleException.getGuardUnityScheduleAssignmentId(),
        includeGuardUnityScheduleAssignment && scheduleException.getGuardUnityScheduleAssignment() != null
            ? new GuardUnityScheduleAssignmentDto(scheduleException.getGuardUnityScheduleAssignment())
            : null,
        scheduleException.getMotive(), scheduleException.getDescription(),
        scheduleException.getDateGuardUnityAssignmentId(), scheduleException.getScheduleMonthlyId(),
        scheduleException.getOrderIndex(), scheduleException.getScheduleExceptionType());
  }


}
