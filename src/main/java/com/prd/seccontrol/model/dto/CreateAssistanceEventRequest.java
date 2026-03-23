package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.AssistanceType;

public record CreateAssistanceEventRequest(
    Long dateGuardUnityAssignmentId,
    AssistanceType assistanceType,
    String photoBase64,
    Double latitude,
    Double longitude
) {

}
