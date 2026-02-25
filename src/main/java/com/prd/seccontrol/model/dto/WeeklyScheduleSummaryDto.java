package com.prd.seccontrol.model.dto;

import java.util.List;

public record WeeklyScheduleSummaryDto(
    Long contractUnityId,
    Long unityId,
    String unityCode,
    String unityName,
    List<DayScheduleSummaryDto> schedules
) {

}
