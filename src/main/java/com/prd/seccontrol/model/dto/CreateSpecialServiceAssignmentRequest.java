package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.TurnType;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateSpecialServiceAssignmentRequest(
    LocalDate date,
    Long guardId,
    Long externalGuardId,
    GuardType guardType,
    LocalTime timeFrom,
    LocalTime timeTo,
    TurnType turnType
) {

}
