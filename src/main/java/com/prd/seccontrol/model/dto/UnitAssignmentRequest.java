package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.ZoneType;

public record UnitAssignmentRequest(
    ZoneType zoneType,
    Long unityId
) {

}
