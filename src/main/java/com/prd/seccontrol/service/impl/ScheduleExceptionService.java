package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateScheduleExceptionRequest;
import com.prd.seccontrol.model.dto.CreateSpecialServiceExceptionRequest;
import com.prd.seccontrol.model.dto.ScheduleExceptionDto;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleExceptionService {

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

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

  public ScheduleExceptionDto createSpecialServiceException(
      CreateSpecialServiceExceptionRequest request) {
    ScheduleException scheduleException = new ScheduleException();
    GuardUnityScheduleAssignment gusa = guardUnityScheduleAssignmentRepository.findByGuardIdAndSpecialServiceUnityScheduleId(
        request.guardId(), request.externalGuardId(), request.scheduleId()).orElse(null);
    if(gusa == null) {
      GuardAssignment ga = new GuardAssignment();
      ga.setExternalGuardId(request.externalGuardId());
      ga.setGuardId(request.guardId());
      ga.setActive(true);
      ga = guardAssignmentRepository.save(ga);
      GuardUnityScheduleAssignment newGusa = new GuardUnityScheduleAssignment();
      newGusa.setGuardAssignmentId(ga.getId());
      newGusa.setGuardType(request.guardType());
      newGusa.setSpecialServiceUnityScheduleId(request.scheduleId());
      gusa = guardUnityScheduleAssignmentRepository.save(newGusa);
    }
    scheduleException.setGuardUnityScheduleAssignmentId(gusa.getId());
    scheduleException.setMotive(request.motive());
    scheduleException.setDescription(request.description());
    scheduleException.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    scheduleException.setScheduleExceptionType(request.scheduleExceptionType());

    ScheduleException savedException = scheduleExceptionRepository.save(scheduleException);
    return new ScheduleExceptionDto(savedException);
  }
}
