package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateEmployeeAssignmentRequest;
import com.prd.seccontrol.model.dto.EmployeeAssignmentMonthlyDto;
import com.prd.seccontrol.model.dto.EmployeeUnitAssignmentDto;
import com.prd.seccontrol.model.dto.UnitAssignmentRequest;
import com.prd.seccontrol.model.entity.EmployeeAssignmentMonthly;
import com.prd.seccontrol.model.entity.EmployeeUnitAssignment;
import com.prd.seccontrol.repository.EmployeeAssignmentMonthlyRepository;
import com.prd.seccontrol.repository.EmployeeUnitAssignmentRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeAssignmentMonthlyService {

  @Autowired
  private EmployeeAssignmentMonthlyRepository employeeAssignmentMonthlyRepository;

  @Autowired
  private EmployeeUnitAssignmentRepository employeeUnitAssignmentRepository;

  public EmployeeAssignmentMonthlyDto findCompleteInformation(Long id) {
    EmployeeAssignmentMonthly assignment = employeeAssignmentMonthlyRepository.findById(id)
        .orElseThrow(
            () -> new RuntimeException("EmployeeAssignmentMonthly not found with id: " + id));

    List<EmployeeUnitAssignment> unitAssignments = employeeUnitAssignmentRepository.findByEmployeeAssignmentMonthlyId(
        assignment.getId());

    List<EmployeeUnitAssignmentDto> unitAssignmentDtos = unitAssignments.stream()
        .map(EmployeeUnitAssignmentDto::new)
        .toList();

    return new EmployeeAssignmentMonthlyDto(assignment, unitAssignmentDtos);
  }

  @Transactional
  public EmployeeAssignmentMonthlyDto createEmployeeAssignmentMonthly(
      CreateEmployeeAssignmentRequest request) {
    EmployeeAssignmentMonthly assignment = new EmployeeAssignmentMonthly();
    assignment.setEmployeeId(request.employeeId());
    assignment.setMonth(request.month());
    assignment.setYear(request.year());
    assignment.setScheduleMonthlyId(request.scheduleMonthlyId());
    assignment.setZoneType(request.zoneType());

    EmployeeAssignmentMonthly savedAssignment = employeeAssignmentMonthlyRepository.save(
        assignment);

    List<EmployeeUnitAssignment> unitAssignments = new ArrayList<>();
    for (UnitAssignmentRequest unitRequest : request.unitAssignments()) {
      EmployeeUnitAssignment unitAssignment = new EmployeeUnitAssignment();
      unitAssignment.setEmployeeAssignmentMonthlyId(savedAssignment.getId());
      unitAssignment.setUnityId(unitRequest.unityId());
      unitAssignment.setZoneType(unitRequest.zoneType());
      unitAssignments.add(unitAssignment);
    }

    employeeUnitAssignmentRepository.saveAll(unitAssignments);
    return new EmployeeAssignmentMonthlyDto(savedAssignment);
  }

  @Transactional
  public EmployeeAssignmentMonthlyDto updateEmployeeAssignmentMonthly(
      CreateEmployeeAssignmentRequest request, Long id) {

    EmployeeAssignmentMonthly existingAssignment = employeeAssignmentMonthlyRepository.findById(id)
        .orElseThrow(
            () -> new RuntimeException("EmployeeAssignmentMonthly not found with id: " + id));

    existingAssignment.setEmployeeId(request.employeeId());
    existingAssignment.setMonth(request.month());
    existingAssignment.setYear(request.year());
    existingAssignment.setScheduleMonthlyId(request.scheduleMonthlyId());
    existingAssignment.setZoneType(request.zoneType());

    EmployeeAssignmentMonthly updatedAssignment = employeeAssignmentMonthlyRepository.save(
        existingAssignment);

    List<Long> newUnityIds = request.unitAssignments().stream()
        .map(UnitAssignmentRequest::unityId)
        .toList();
    employeeUnitAssignmentRepository.deleteByUnityIdNotInAndEmployeeAssignmentMonthlyId(
        newUnityIds, updatedAssignment.getId());

    List<EmployeeUnitAssignment> existingUnitAssignments = employeeUnitAssignmentRepository.findByUnityIdIn(newUnityIds);

    //update existing in request and save new unities in request
    for (UnitAssignmentRequest unitRequest : request.unitAssignments()) {
      EmployeeUnitAssignment unitAssignment = existingUnitAssignments.stream()
          .filter(e -> e.getUnityId().equals(unitRequest.unityId()))
          .findFirst()
          .orElseGet(() -> {
            EmployeeUnitAssignment newUnitAssignment = new EmployeeUnitAssignment();
            newUnitAssignment.setEmployeeAssignmentMonthlyId(updatedAssignment.getId());
            newUnitAssignment.setUnityId(unitRequest.unityId());
            newUnitAssignment.setZoneType(unitRequest.zoneType());
            return newUnitAssignment;
          });
      unitAssignment.setZoneType(unitRequest.zoneType());
      employeeUnitAssignmentRepository.save(unitAssignment);
    }

    return new EmployeeAssignmentMonthlyDto(updatedAssignment);
  }
}
