package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.EmployeeAssignmentMonthly;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.model.types.ZoneType;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public record EmployeeAssignmentMonthlyDto(
    Long id,
    Long employeeId,
    EmployeeSummaryDto employee,
    Long scheduleMonthlyId,
    ScheduleMonthly scheduleMonthly,
    ZoneType zoneType,
    Month month,
    Integer year,
    LocalDateTime createdAt,
    List<EmployeeUnitAssignmentDto> unitAssignments
) {
  public EmployeeAssignmentMonthlyDto(EmployeeAssignmentMonthly employeeAssignmentMonthly, List<EmployeeUnitAssignmentDto> unitAssignments) {
    this(
        employeeAssignmentMonthly.getId(),
        employeeAssignmentMonthly.getEmployeeId(),
        new EmployeeSummaryDto(employeeAssignmentMonthly.getEmployee()),
        employeeAssignmentMonthly.getScheduleMonthlyId(),
        employeeAssignmentMonthly.getScheduleMonthly(),
        employeeAssignmentMonthly.getZoneType(),
        employeeAssignmentMonthly.getMonth(),
        employeeAssignmentMonthly.getYear(),
        employeeAssignmentMonthly.getCreatedAt(),
        unitAssignments // unitAssignments will be set separately after fetching from the database
    );
  }

  public EmployeeAssignmentMonthlyDto(EmployeeAssignmentMonthly employeeAssignmentMonthly){
    this(
        employeeAssignmentMonthly.getId(),
        employeeAssignmentMonthly.getEmployeeId(),
        employeeAssignmentMonthly.getEmployee() != null ? new EmployeeSummaryDto(employeeAssignmentMonthly.getEmployee()) : null,
        employeeAssignmentMonthly.getScheduleMonthlyId(),
        employeeAssignmentMonthly.getScheduleMonthly(),
        employeeAssignmentMonthly.getZoneType(),
        employeeAssignmentMonthly.getMonth(),
        employeeAssignmentMonthly.getYear(),
        employeeAssignmentMonthly.getCreatedAt(),
        null // unitAssignments will be set separately after fetching from the database
    );
  }
}
