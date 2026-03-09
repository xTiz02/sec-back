package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateScheduleExceptionRequest;
import com.prd.seccontrol.model.dto.ScheduleExceptionDto;
import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleExceptionService {

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  public ScheduleExceptionDto createScheduleException(CreateScheduleExceptionRequest request) {
    ScheduleException scheduleException = new ScheduleException();
    scheduleException.setGuardUnityScheduleAssignmentId(request.guardUnityScheduleAssignmentId());
    scheduleException.setMotive(request.motive());
    scheduleException.setDescription(request.description());
    scheduleException.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    scheduleException.setScheduleMonthlyId(request.scheduleMonthlyId());
    scheduleException.setScheduleExceptionType(request.scheduleExceptionType());

    ScheduleException savedException = scheduleExceptionRepository.save(scheduleException);
    return new ScheduleExceptionDto(savedException);
  }
}
