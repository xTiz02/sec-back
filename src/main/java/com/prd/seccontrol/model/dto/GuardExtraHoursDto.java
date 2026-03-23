package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardExtraHours;
import java.time.LocalDate;
import java.time.LocalTime;

public record GuardExtraHoursDto(
    Long id,
    LocalDate startDate,
    LocalTime startTime,
    LocalDate endDate,
    LocalTime endTime,
    Integer extraHours,
    String coveredGuardName
) {

  public GuardExtraHoursDto(
      GuardExtraHours guardExtraHours,
      String coveredGuardName
  ) {
    this(
        guardExtraHours.getId(),
        guardExtraHours.getStartDate(),
        guardExtraHours.getStartTime(),
        guardExtraHours.getEndDate(),
        guardExtraHours.getEndTime(),
        guardExtraHours.getExtraHours(),
        coveredGuardName
    );
  }

}
