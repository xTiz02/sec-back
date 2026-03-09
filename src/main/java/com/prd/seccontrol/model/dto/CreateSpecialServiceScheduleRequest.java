package com.prd.seccontrol.model.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateSpecialServiceScheduleRequest(
    Long specialServiceUnityId,
    LocalDate dateFrom,
    LocalDate dateTo,
    List<CreateSpecialServiceAssignmentRequest> assignments
) {

}
