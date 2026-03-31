package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.TurnType;
import java.time.LocalTime;

public record ContractScheduleSummaryDto(
    Long contractUnityId,
    String unityCode,
    TurnType turnType,
    DayOfWeek dayOfWeek,
    Long turnAndHourId,
    LocalTime startTime,
    LocalTime endTime
) {


}
