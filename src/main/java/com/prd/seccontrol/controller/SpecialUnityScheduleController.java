package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateSpecialServiceScheduleRequest;
import com.prd.seccontrol.model.dto.SpecialServiceDayAssignmentDto;
import com.prd.seccontrol.model.dto.SpecialServiceGuardUnityAssignmentDto;
import com.prd.seccontrol.model.dto.SpecialServiceScheduleDto;
import com.prd.seccontrol.model.dto.SpecialServiceScheduleSummaryDto;
import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import com.prd.seccontrol.repository.SpecialServiceUnityScheduleRepository;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.service.impl.SpecialUnityScheduleService;
import com.prd.seccontrol.util.SEConstants;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialUnityScheduleController {

  @Autowired
  private SpecialServiceUnityScheduleRepository specialServiceUnityScheduleRepository;

  @Autowired
  private SpecialUnityScheduleService specialUnityScheduleService;

  @Autowired
  private SearchService<SpecialServiceUnitySchedule> searchService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-schedule/all")
  public Page<SpecialServiceScheduleSummaryDto> getSpecialServiceUnitySchedules(
      @RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, SpecialServiceUnitySchedule.class).map(
        SpecialServiceScheduleSummaryDto::new
    );
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-schedule")
  public SpecialServiceScheduleDto createSpecialServiceUnitySchedule(
      @RequestBody CreateSpecialServiceScheduleRequest request) {
    return specialUnityScheduleService.createSpecialServiceUnitySchedule(request);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-schedule/{scheduleId}")
  public SpecialServiceScheduleDto getSpecialServiceUnityScheduleById(@PathVariable Long scheduleId) {

    return specialUnityScheduleService.getSpecialServiceUnitySchedule(scheduleId);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-schedule/{scheduleId}/guard-pool")
  public List<SpecialServiceGuardUnityAssignmentDto> getSpecialServiceGuardsSchedulesById(@PathVariable Long scheduleId) {

    return specialUnityScheduleService.getGuardAssignmentsForSpecialServiceUnitySchedule(scheduleId);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-schedule/{scheduleId}/day-assignments")
  public List<SpecialServiceDayAssignmentDto> getSpecialServiceGuardsSchedulesById(@PathVariable Long scheduleId, @RequestParam
      LocalDate date) {

    return specialUnityScheduleService.getSpecialServiceUnitySchedule(scheduleId).dayAssignments().stream().filter(
        dayAssignment -> dayAssignment.date().equals(date)
    ).toList();
  }
}
