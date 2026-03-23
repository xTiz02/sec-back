package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardRequest;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.model.types.RequestStatus;
import com.prd.seccontrol.model.types.RequestType;
import java.time.LocalDateTime;

public record GuardRequestDto(
    Long id,
    Long guardAssistanceEventId,
    AssistanceType assistanceType,
    String description,
    RequestType requestType,
    RequestStatus requestStatus,
    LocalDateTime createdAt
) {
  public GuardRequestDto(GuardRequest guardRequest, AssistanceType assistanceType) {
    this(
        guardRequest.getId(),
        guardRequest.getGuardAssistanceEventId(),
        assistanceType,
        guardRequest.getDescription(),
        guardRequest.getRequestType(),
        guardRequest.getRequestStatus(),
        guardRequest.getCreatedAt()
    );
  }

}
