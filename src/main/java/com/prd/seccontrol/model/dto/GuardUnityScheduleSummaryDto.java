package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;

public record GuardUnityScheduleSummaryDto(
    Long gusaId,
    Long guardId,
    String guardCode,
    GuardType guardType
) {

}
