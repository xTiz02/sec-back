package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.types.AssistanceProblemType;
import com.prd.seccontrol.model.types.AssistanceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record GuardAssistanceEventDto(
    Long id,
    Long dateGuardUnityAssignmentId,
    String photoUrl,
    LocalDate markDate,
    LocalTime markTime,
    LocalDateTime systemMark,
    Integer differenceInMinutes,
    Integer numberOrder,
    AssistanceType assistanceType,
    AssistanceProblemType assistanceProblemType
) {

  public GuardAssistanceEventDto(GuardAssistanceEvent event) {
    this(
        event.getId(),
        event.getDateGuardUnityAssignmentId(),
        event.getPhotoUrl(),
        event.getMarkDate(),
        event.getMarkTime(),
        event.getSystemMark(),
        event.getDifferenceInMinutes(),
        event.getNumberOrder(),
        event.getAssistanceType(),
        event.getAssistanceProblemType()
    );
  }
}
