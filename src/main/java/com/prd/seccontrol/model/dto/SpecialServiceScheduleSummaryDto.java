package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SpecialServiceScheduleSummaryDto(
    Long id,
    Long specialServiceUnityId,
    String specialServiceUnityName,
    Integer totalDays,
    Integer totalAssignments,
    LocalDate dateFrom,
    LocalDate dateTo,
    LocalDateTime createdAt
) {
  public SpecialServiceScheduleSummaryDto(SpecialServiceUnitySchedule schedule) {
    this(
        schedule.getId(),
        schedule.getSpecialServiceUnityId(),
        schedule.getSpecialServiceUnity() != null ? schedule.getSpecialServiceUnity().getUnityName() : null,
        schedule.getTotalDays(),
        schedule.getTotalAssignments(),
        schedule.getDateFrom(),
        schedule.getDateTo(),
        schedule.getCreatedAt()
    );
  }
}
