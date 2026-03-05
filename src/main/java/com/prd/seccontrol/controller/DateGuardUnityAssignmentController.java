package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateBulkFreeDayRequest;
import com.prd.seccontrol.model.dto.CreateBulkVacationRequest;
import com.prd.seccontrol.model.dto.CreateDailyAssignmentRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentDto;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.service.impl.DateGuardUnityAssignmentService;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateGuardUnityAssignmentController {

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private DateGuardUnityAssignmentService dateGuardUnityAssignmentService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/calendar")
  public List<DateGuardUnityAssignmentDto> getDateGuardUnityAssignmentsForCalendar(
      @RequestParam Long contractUnityId, @RequestParam Long scheduleMonthlyId) {
    return dateGuardUnityAssignmentRepository.findByContractUnityIdAndScheduleMonthlyId(
            contractUnityId, scheduleMonthlyId)
        .stream()
        .map(DateGuardUnityAssignmentDto::new)
        .toList();
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment")
  public DateGuardUnityAssignmentDto createDateGuardUnityAssignment(
      @RequestBody CreateDailyAssignmentRequest request) {
    return dateGuardUnityAssignmentService.createDateGuardUnityAssignment(request,null);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/bulk-free-days")
  public List<DateGuardUnityAssignmentDto> bulkCreateFreeDayAssignments(
      @RequestBody CreateBulkFreeDayRequest requests) {
    return dateGuardUnityAssignmentService.bulkCreateFreeDayAssignments(requests);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/vacation")
  public List<DateGuardUnityAssignmentDto> bulkCreateVacationAssignments(
      @RequestBody CreateBulkVacationRequest requests) {
    return dateGuardUnityAssignmentService.bulkCreateVacationAssignments(requests);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/{dateGuardUnityAssignmentId}")
  public Long deleteDateGuardUnityAssignment(@PathVariable Long dateGuardUnityAssignmentId) {
    dateGuardUnityAssignmentRepository.deleteById(dateGuardUnityAssignmentId);
    return dateGuardUnityAssignmentId;
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/vacation/{dateGuardUnityAssignmentId}")
  public Long deleteDateGuardUnityAssignmentVacation(@PathVariable Long dateGuardUnityAssignmentId) {
    dateGuardUnityAssignmentRepository.deleteById(dateGuardUnityAssignmentId);
    return dateGuardUnityAssignmentId;
  }

}
