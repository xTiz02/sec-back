package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import com.prd.seccontrol.model.types.DayOfWeek;

public record ContractScheduleUnitTemplateDto(
    Long id,
    Long contractUnityId,
    DayOfWeek dayOfWeek,
    Integer numOfGuards,
    Integer numTurnDay,
    Integer numTurnNight
) {

  public ContractScheduleUnitTemplateDto(ContractScheduleUnitTemplate scheduleUnitTemplate) {
    this(scheduleUnitTemplate.getId(), scheduleUnitTemplate.getContractUnityId(),
        scheduleUnitTemplate.getDayOfWeek(), scheduleUnitTemplate.getNumOfGuards(),
        scheduleUnitTemplate.getNumTurnDay(), scheduleUnitTemplate.getNumTurnNight());
  }
}
