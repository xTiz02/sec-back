package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.SpecialServiceUnity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SpecialServiceScheduleDto(
    Long id,
    Long specialServiceUnityId,
    SpecialServiceUnity specialServiceUnity,
    LocalDate dateFrom,
    LocalDate dateTo,
    Integer totalDays,
    Integer totalAssignments,
    List<SpecialServiceDayAssignmentDto> dayAssignments,
    LocalDateTime createdAt
) {

}
