package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateEmployeeAssignmentRequest;
import com.prd.seccontrol.model.dto.EmployeeAssignmentMonthlyDto;
import com.prd.seccontrol.model.entity.EmployeeAssignmentMonthly;
import com.prd.seccontrol.repository.EmployeeAssignmentMonthlyRepository;
import com.prd.seccontrol.service.impl.EmployeeAssignmentMonthlyService;
import com.prd.seccontrol.service.inter.SearchService;
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
public class EmployeeAssignmentMonthlyController {

  @Autowired
  private EmployeeAssignmentMonthlyRepository employeeAssignmentMonthlyRepository;

  @Autowired
  private SearchService<EmployeeAssignmentMonthly> searchService;

  @Autowired
  private EmployeeAssignmentMonthlyService employeeAssignmentMonthlyService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee-assignment-monthly/all")
  public Page<EmployeeAssignmentMonthlyDto> findAll(@RequestParam(required = false) String query,
      Pageable pageable) {
    return searchService.search(query, pageable, EmployeeAssignmentMonthly.class)
        .map(EmployeeAssignmentMonthlyDto::new);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee-assignment-monthly/{id}")
  public EmployeeAssignmentMonthlyDto findById(@PathVariable Long id) {
    return employeeAssignmentMonthlyService.findCompleteInformation(id);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee-assignment-monthly")
  public EmployeeAssignmentMonthlyDto createEmployeeAssignmentMonthly(@RequestBody
  CreateEmployeeAssignmentRequest request) {
    return employeeAssignmentMonthlyService.createEmployeeAssignmentMonthly(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee-assignment-monthly/{id}")
  public EmployeeAssignmentMonthlyDto updateEmployeeAssignmentMonthly(@RequestBody
  CreateEmployeeAssignmentRequest request, @PathVariable Long id) {
    return employeeAssignmentMonthlyService.updateEmployeeAssignmentMonthly(request, id);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/employee-assignment-monthly/{id}")
  public Long deleteEmployeeAssignmentMonthly(@PathVariable Long id) {
    employeeAssignmentMonthlyRepository.deleteById(id);
    return id;
  }


}
