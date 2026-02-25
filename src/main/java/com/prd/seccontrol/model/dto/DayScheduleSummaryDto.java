package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.DayOfWeek;
import java.util.List;

public record DayScheduleSummaryDto(
    DayOfWeek dayOfWeek,
    Long scheduleId,
    Integer numOfGuards,
    Integer numTurnDay,
    Integer numTurnNight,
    List<TurnTemplate> turns
) {
  public DayScheduleSummaryDto(ContractScheduleUnitTemplate scheduleUnitTemplate, List<TurnTemplate> turns) {
    this(scheduleUnitTemplate.getDayOfWeek(), scheduleUnitTemplate.getId(),
        scheduleUnitTemplate.getNumOfGuards(), scheduleUnitTemplate.getNumTurnDay(),
        scheduleUnitTemplate.getNumTurnNight(), turns);
  }

}
