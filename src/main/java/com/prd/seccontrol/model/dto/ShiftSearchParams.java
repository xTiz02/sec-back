package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;

public record ShiftSearchParams(
    LocalDate dateFrom,
    LocalDate dateTo,
    Long clientId,
    Long guardId,
    Long externalGuardId,
    Long unityId,
    Long specialServiceUnityId,
    ScheduleAssignmentType scheduleAssignmentType,
    Boolean hasExceptions,
    Boolean hasExtraHours,
    Boolean hasMarks,
    Integer page,
    Integer size
) {

}
