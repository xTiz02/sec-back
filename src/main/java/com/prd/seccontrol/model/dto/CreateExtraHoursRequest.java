package com.prd.seccontrol.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateExtraHoursRequest(

    Long principalDateGuardUnityAssignmentId,
    Long coverDateGuardUnityAssignmentId,
    LocalDate startDate,
    LocalTime startTime,
    LocalDate endDate,
    LocalTime endTime
) {

}
