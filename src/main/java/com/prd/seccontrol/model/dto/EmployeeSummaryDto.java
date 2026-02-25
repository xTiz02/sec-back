package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.Employee;

public record EmployeeSummaryDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    String documentNumber
) {

  public EmployeeSummaryDto(Employee employee) {
    this(employee.getId(), employee.getFirstName(), employee.getLastName(),
        employee.getEmail(), employee.getDocumentNumber());
  }
}
