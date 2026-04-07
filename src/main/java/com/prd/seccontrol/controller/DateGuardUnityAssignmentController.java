package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateBulkFreeDayRequest;
import com.prd.seccontrol.model.dto.CreateBulkVacationRequest;
import com.prd.seccontrol.model.dto.CreateDailyAssignmentRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentDto;
import com.prd.seccontrol.model.dto.NextShiftToCoverDto;
import com.prd.seccontrol.model.dto.ShiftDetailDto;
import com.prd.seccontrol.model.dto.ShiftSearchParams;
import com.prd.seccontrol.model.dto.ShiftSearchResultDto;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.service.impl.DateGuardUnityAssignmentService;
import com.prd.seccontrol.service.impl.ShiftFilterService;
import com.prd.seccontrol.util.SEConstants;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Autowired
  private ShiftFilterService shiftFilterService;

  // todo aligerar esto mucho
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
    return dateGuardUnityAssignmentService.deleteDateGuardUnityAssignment(dateGuardUnityAssignmentId);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/vacation/{dateGuardUnityAssignmentId}")
  public Long deleteDateGuardUnityAssignmentVacation(@PathVariable Long dateGuardUnityAssignmentId) {
    return dateGuardUnityAssignmentService.deleteDateGuardUnityAssignment(dateGuardUnityAssignmentId);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/search")
  public Page<ShiftSearchResultDto> searchShifts(
      @RequestParam LocalDate dateFrom,
      @RequestParam LocalDate dateTo,
      @RequestParam(required = false) Long clientId,
      @RequestParam(required = false) Long guardId,
      @RequestParam(required = false) Long externalGuardId,
      @RequestParam(required = false) Long unityId,
      @RequestParam(required = false) Long specialServiceUnityId,
      @RequestParam(required = false) String scheduleAssignmentType,
      @RequestParam(required = false) Boolean hasExceptions,
      @RequestParam(required = false) Boolean hasExtraHours,
      @RequestParam(required = false) Boolean hasMarks,
  Pageable pageable) {

    ShiftSearchParams params = new ShiftSearchParams(
        dateFrom,
        dateTo,
        clientId,
        guardId,
        externalGuardId,
        unityId,
        specialServiceUnityId,
        scheduleAssignmentType != null ? ScheduleAssignmentType.valueOf(scheduleAssignmentType) : null,
        hasExceptions,
        hasExtraHours,
        hasMarks,
        pageable.getPageNumber(),
        pageable.getPageSize()
    );

    return shiftFilterService.getDateGuardShiftsBYParam(params);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/{id}/detail")
  public ShiftDetailDto getDateGuardUnityAssignmentById(@PathVariable Long id) {
    return shiftFilterService.getShiftDetailById(id);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/date-guard-unity-assignment/{id}/next-shifts-to-cover")
  public List<NextShiftToCoverDto> findNextShiftsToCoverForGuard(@PathVariable Long id) {
    return dateGuardUnityAssignmentService.getNextShiftsToCoverForGuard(id);
  }

}
