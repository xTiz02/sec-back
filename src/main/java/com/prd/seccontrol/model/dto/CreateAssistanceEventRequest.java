package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.AssistanceType;
import org.springframework.web.multipart.MultipartFile;

public record CreateAssistanceEventRequest(
    Long dateGuardUnityAssignmentId,
    AssistanceType assistanceType,
    MultipartFile photo,
    Double latitude,
    Double longitude
) {

}
