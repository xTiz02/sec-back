package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnTemplate;
import java.time.LocalDate;

public record GuardShiftDto(
    Long dateGuardUnityAssignmentId,
    LocalDate date,
    ContractUnityInfo contractUnity,
    TurnTemplate turnTemplate
) {

}
