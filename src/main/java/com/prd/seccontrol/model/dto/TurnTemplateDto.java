package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.entity.TurnTemplate;

public record TurnTemplateDto(
    Long id,
    Long scheduleUnitTemplateId,
    Long turnTemplateId,
    TurnTemplate turnTemplate
) {

  public TurnTemplateDto(TurnAndHour turnAndHour) {
    this(turnAndHour.getId(), turnAndHour.getContractScheduleUnitTemplateId(),
        turnAndHour.getTurnTemplateId(), turnAndHour.getTurnTemplate());
  }
}
