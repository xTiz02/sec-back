package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateScheduleExceptionRequest;
import com.prd.seccontrol.model.dto.CreateSpecialServiceExceptionRequest;
import com.prd.seccontrol.model.dto.ScheduleExceptionDto;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleExceptionService {

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Transactional
  public ScheduleExceptionDto createScheduleException(CreateScheduleExceptionRequest request) {

    GuardUnityScheduleAssignment gusa = guardUnityScheduleAssignmentRepository.findByGuardIdAndScheduleMonthlyId(
        request.guardId(), request.externalGuardId(), request.scheduleMonthlyId()).orElse(null);
    DateGuardUnityAssignment dateAssignment = dateGuardUnityAssignmentRepository.findById(
        request.dateGuardUnityAssignmentId()).orElseThrow(() -> new RuntimeException("DateGuardUnityAssignment not found with id: " + request.dateGuardUnityAssignmentId()));

    List<ScheduleException> existingException = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(dateAssignment.getId());
    if(!existingException.isEmpty()) {
      throw new RuntimeException("Ya existe una excepción para esta asignación en la fecha especificada.");
    }

    if (gusa == null) {
      GuardAssignment ga = new GuardAssignment();
      ga.setExternalGuardId(request.externalGuardId());
      ga.setGuardId(request.guardId());
      ga.setActive(true);
      ga = guardAssignmentRepository.save(ga);
      GuardUnityScheduleAssignment newGusa = new GuardUnityScheduleAssignment();
      newGusa.setGuardAssignmentId(ga.getId());
      newGusa.setGuardType(request.guardType());
      newGusa.setScheduleMonthlyId(request.scheduleMonthlyId());
      newGusa.setContractUnityId(
          dateAssignment.getGuardUnityScheduleAssignment().getContractUnityId());
      gusa = guardUnityScheduleAssignmentRepository.save(newGusa);
    }
    ScheduleException scheduleException = new ScheduleException();
    scheduleException.setGuardUnityScheduleAssignmentId(gusa.getId());
    scheduleException.setDescription(request.motive());
    scheduleException.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    scheduleException.setScheduleMonthlyId(request.scheduleMonthlyId());
    scheduleException.setScheduleExceptionType(request.scheduleExceptionType());

    ScheduleException savedException = scheduleExceptionRepository.save(scheduleException);
    if (!dateAssignment.isHasExceptions()) {
      dateGuardUnityAssignmentRepository.updateHasExceptionsById(true, dateAssignment.getId());
    }
    return new ScheduleExceptionDto(savedException, false);
  }

  @Transactional
  public ScheduleExceptionDto createSpecialServiceException(
      CreateSpecialServiceExceptionRequest request) {
    ScheduleException scheduleException = new ScheduleException();
    GuardUnityScheduleAssignment gusa = guardUnityScheduleAssignmentRepository.findByGuardIdAndSpecialServiceUnityScheduleId(
        request.guardId(), request.externalGuardId(), request.scheduleId()).orElse(null);
    DateGuardUnityAssignment dateAssignment = dateGuardUnityAssignmentRepository.findById(
        request.dateGuardUnityAssignmentId()).orElseThrow(() -> new RuntimeException("DateGuardUnityAssignment not found with id: " + request.dateGuardUnityAssignmentId()));

    List<ScheduleException> existingException = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(dateAssignment.getId());
    if(!existingException.isEmpty()) {
      throw new RuntimeException("Ya existe una excepción para esta asignación en la fecha especificada.");
    }

    if (gusa == null) {
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
    scheduleException.setDescription(request.description());
    scheduleException.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    scheduleException.setScheduleExceptionType(request.scheduleExceptionType());

    ScheduleException savedException = scheduleExceptionRepository.save(scheduleException);
    if (!dateAssignment.isHasExceptions()) {
      dateGuardUnityAssignmentRepository.updateHasExceptionsById(true, dateAssignment.getId());
    }
    return new ScheduleExceptionDto(savedException, true);
  }

  @Transactional
  public Long deleteScheduleException(Long scheduleExceptionId) {
    ScheduleException scheduleException = scheduleExceptionRepository.findById(scheduleExceptionId)
        .orElse(null);
    GuardUnityScheduleAssignment gusa = scheduleException.getGuardUnityScheduleAssignment();
    List<DateGuardUnityAssignment> dateAssignments = dateGuardUnityAssignmentRepository.findByGuardUnityScheduleAssignmentId(
        gusa.getId());
    int dateAssignmentCount = dateAssignments.size();
    scheduleExceptionRepository.deleteById(scheduleExceptionId);
    if (dateAssignmentCount == 0) {
      guardUnityScheduleAssignmentRepository.deleteById(scheduleExceptionId);
      guardAssignmentRepository.deleteById(gusa.getGuardAssignmentId());
    }

    List<ScheduleException> exceptions = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(
        scheduleException.getDateGuardUnityAssignmentId());
    if (exceptions.isEmpty()) {
      dateGuardUnityAssignmentRepository.updateHasExceptionsById(false,
          scheduleException.getDateGuardUnityAssignmentId());
    }

    return scheduleExceptionId;
  }
}
