package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.SpecialServiceUnity;
import com.prd.seccontrol.model.entity.TurnTemplate;
import java.time.LocalDate;

public record GuardShiftDto(
    Long dateGuardUnityAssignmentId,
    LocalDate date,
    ContractUnityInfo contractUnity,
    SpecialServiceUnity specialServiceUnity,
    TurnTemplate turnTemplate
) {

}
