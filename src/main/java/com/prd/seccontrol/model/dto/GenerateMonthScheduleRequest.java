package com.prd.seccontrol.model.dto;

import java.time.Month;

public record GenerateMonthScheduleRequest(
    Long contractUnityId,
    Month month,
    Integer year,
    String scheduleName,
    String scheduleDescription
) {

}
