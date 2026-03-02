package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.entity.TurnTemplate;

public record TurnAndHourDto(
    Long id,
    Long contractScheduleUnitTemplateId,
    Long turnTemplateId,
    TurnTemplate turnTemplate
) {

  public TurnAndHourDto(TurnAndHour turnAndHour) {
    this(turnAndHour.getId(), turnAndHour.getContractScheduleUnitTemplateId(),
        turnAndHour.getTurnTemplateId(), turnAndHour.getTurnTemplate());
  }

}
