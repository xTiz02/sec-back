package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.ZoneType;
import java.time.Month;
import java.util.List;

public record CreateEmployeeAssignmentRequest(
    Long employeeId,
    Long scheduleMonthlyId,
    ZoneType zoneType,
    Integer year,
    Month month,
    List<UnitAssignmentRequest> unitAssignments
) {

}
