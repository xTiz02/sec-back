package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;

public record CreateGuardRequest(

    Long employeeId,
    String licenseNumber,
    GuardType guardType,
    String photoUrl
) {

}
