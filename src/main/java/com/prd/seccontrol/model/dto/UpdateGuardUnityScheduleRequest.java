package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;

public record UpdateGuardUnityScheduleRequest(
    GuardType guardType
) {

}
