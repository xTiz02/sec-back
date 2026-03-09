package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.entity.TurnTemplate;

public record TurnAndHourDto(
    Long id,
    Long contractScheduleUnitTemplateId,
    Long specialServiceUnityScheduleId,
    Long turnTemplateId,
    TurnTemplate turnTemplate
) {

  public TurnAndHourDto(TurnAndHour turnAndHour) {
    this(turnAndHour.getId(), turnAndHour.getContractScheduleUnitTemplateId(),
        turnAndHour.getSpecialServiceUnityScheduleId(),
        turnAndHour.getTurnTemplateId(), turnAndHour.getTurnTemplate());
  }

}
