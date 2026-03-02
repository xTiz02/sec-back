package com.prd.seccontrol.model.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateBulkFreeDayRequest(
    Long guardUnityScheduleAssignmentId,
    List<LocalDate> dates
) {

}
