package com.prd.seccontrol.model.dto;

import java.util.List;

public record UnitWeeklySchedule(
    Long contractUnityId,
    Long unityId,
    List<DayTurnAssignment> days
) {

}
