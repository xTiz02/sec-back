package com.prd.seccontrol.model.dto;

import java.time.LocalDate;

public record CreateBulkVacationRequest(
  LocalDate dateFrom,
  LocalDate dateTo,
  Long guardUnityScheduleAssignmentId
) {

}
