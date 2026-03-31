package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateEmployeeRequest;
import com.prd.seccontrol.model.dto.EmployeeDto;
import com.prd.seccontrol.model.entity.Employee;
import com.prd.seccontrol.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public EmployeeDto createEmployee(CreateEmployeeRequest request) {
    Employee employee = new Employee();
    employee.setFirstName(request.firstName());
    employee.setLastName(request.lastName());
    employee.setEmail(request.email());
    employee.setAddress(request.address());
    employee.setBirthDate(request.birthDate());
    employee.setDocumentNumber(request.documentNumber());
    employee.setMobilePhone(request.mobilePhone());
    employee.setUserId(request.userId());
    employee.setGender(request.gender());
    employee.setIdentificationType(request.identificationType());
    employee.setEmployeeType(request.employeeType());

    Employee savedEmployee = employeeRepository.save(employee);
    return new EmployeeDto(savedEmployee);
  }

  @Transactional
  public EmployeeDto updateEmployee(CreateEmployeeRequest request, Long id) {
    Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    employee.setFirstName(request.firstName() != null ? request.firstName() : employee.getFirstName());
    employee.setLastName(request.lastName() != null ? request.lastName() : employee.getLastName());
    employee.setEmail(request.email() != null ? request.email() : employee.getEmail());
    employee.setAddress(request.address() != null ? request.address() : employee.getAddress());
    employee.setBirthDate(request.birthDate() != null ? request.birthDate() : employee.getBirthDate());
    employee.setDocumentNumber(request.documentNumber() != null ? request.documentNumber() : employee.getDocumentNumber());
    employee.setMobilePhone(request.mobilePhone() != null ? request.mobilePhone() : employee.getMobilePhone());
    employee.setGender(request.gender() != null ? request.gender() : employee.getGender());
    employee.setIdentificationType(request.identificationType() != null ? request.identificationType() : employee.getIdentificationType());
    employee.setEmployeeType(request.employeeType() != null ? request.employeeType() : employee.getEmployeeType());

    Employee existingUserIdEmployee = employeeRepository.findByUserId(request.userId()).orElse(null);
    if (request.userId() != null && existingUserIdEmployee != null && !existingUserIdEmployee.getId().equals(id)) {
      throw new RuntimeException("El userId ya está asociado a otro empleado.");
    }
    employee.setUserId(request.userId() != null ? request.userId() : employee.getUserId());
    return new EmployeeDto(employeeRepository.save(employee));
  }
}
