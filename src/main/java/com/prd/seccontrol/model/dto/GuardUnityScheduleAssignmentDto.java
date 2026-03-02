package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.model.types.GuardType;

public record GuardUnityScheduleAssignmentDto(
    Long id,
    Long scheduleMonthlyId,
    ScheduleMonthly scheduleMonthly,
    Long guardAssignmentId,
    GuardAssignmentDto guardAssignment,
    GuardType guardType,
    Long contractUnityId,
    ContractUnityDto contractUnity
) {

  public GuardUnityScheduleAssignmentDto(GuardUnityScheduleAssignment assignment) {
    this(
        assignment.getId(),
        assignment.getScheduleMonthlyId(),
        assignment.getScheduleMonthly(),
        assignment.getGuardAssignmentId(),
        assignment.getGuardAssignment() != null ? new GuardAssignmentDto(assignment.getGuardAssignment()) : null,
        assignment.getGuardType(),
        assignment.getContractUnityId(),
        assignment.getContractUnity() != null ? new ContractUnityDto(assignment.getContractUnity()) : null
    );
  }
}
