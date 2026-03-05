package com.prd.seccontrol.model.dto;

import java.time.LocalDate;

public record CreateBulkVacationRequest(
  LocalDate date,
  LocalDate toDate,
  Long guardUnityScheduleAssignmentId
) {

}
