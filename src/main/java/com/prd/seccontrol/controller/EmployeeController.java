package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateEmployeeRequest;
import com.prd.seccontrol.model.dto.EmployeeDto;
import com.prd.seccontrol.model.entity.Employee;
import com.prd.seccontrol.repository.EmployeeRepository;
import com.prd.seccontrol.service.impl.EmployeeService;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  @Autowired
  private SearchService<Employee> employeeSearchService;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeService employeeService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee/all")
  public Page<EmployeeDto> findAll(@RequestParam(required = false) String query, Pageable pageable) {
    Page<Employee> page = employeeSearchService.search(query, pageable, Employee.class);
    return page.map(EmployeeDto::new);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee/{id}")
  public EmployeeDto findById(@PathVariable Long id) throws Exception {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new Exception("Employee not found with id: " + id));
    return new EmployeeDto(employee);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee")
  public EmployeeDto createEmployee(@RequestBody CreateEmployeeRequest request) {
    return employeeService.createEmployee(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee/{id}")
  public EmployeeDto updateEmployee(@PathVariable Long id, @RequestBody CreateEmployeeRequest request) throws Exception {
    return employeeService.updateEmployee(request, id);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee/{id}")
  public Long deleteEmployee(@PathVariable Long id) throws Exception {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new Exception("Employee not found with id: " + id));
    employeeRepository.delete(employee);
    return id;
  }


}
