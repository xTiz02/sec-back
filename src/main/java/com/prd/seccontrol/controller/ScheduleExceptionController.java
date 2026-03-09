package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateScheduleExceptionRequest;
import com.prd.seccontrol.model.dto.ScheduleExceptionDto;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import com.prd.seccontrol.service.impl.ScheduleExceptionService;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleExceptionController {

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  @Autowired
  private ScheduleExceptionService scheduleExceptionService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-exception/by-date-assignment/{dateGuardUnityAssignmentId}")
  public List<ScheduleExceptionDto> getScheduleExceptionsByDateGuardUnityAssignmentId(
      @PathVariable Long dateGuardUnityAssignmentId) {
    return scheduleExceptionRepository.findByDateGuardUnityAssignmentId(dateGuardUnityAssignmentId)
        .stream()
        .map(ScheduleExceptionDto::new)
        .toList();
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-exception")
  public ScheduleExceptionDto createScheduleException(
      @RequestBody CreateScheduleExceptionRequest request) {
    return scheduleExceptionService.createScheduleException(request);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/schedule-exception/{scheduleExceptionId}")
  public Long deleteScheduleException(@PathVariable Long scheduleExceptionId) {
    scheduleExceptionRepository.deleteById(scheduleExceptionId);
    return scheduleExceptionId;
  }
}
