package com.prd.seccontrol.model.dto;

import java.util.List;

public record AssignTurnsToWeekRequest(
    Long contractId,
    List<UnitWeeklySchedule> units
) {

}
