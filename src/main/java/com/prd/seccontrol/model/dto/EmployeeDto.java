package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.Employee;
import com.prd.seccontrol.model.types.Country;
import com.prd.seccontrol.model.types.Gender;
import com.prd.seccontrol.model.types.IdentificationType;
import java.time.LocalDate;

public record EmployeeDto(
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String mobilePhone,
    String email,
    String address,
    String documentNumber,
    Country country,
    Gender gender,
    IdentificationType identificationType,
    LocalDate birthDate
) {

  public EmployeeDto(Employee employee) {
    this(employee.getId(), employee.getUserId(), employee.getFirstName(), employee.getLastName(),
        employee.getMobilePhone(), employee.getEmail(), employee.getAddress(),
        employee.getDocumentNumber(), employee.getCountry(), employee.getGender(),
        employee.getIdentificationType(), employee.getBirthDate());
  }

}
