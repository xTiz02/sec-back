package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.GuardShiftBaseDTO;
import com.prd.seccontrol.model.dto.GuardShiftDetailDto;
import com.prd.seccontrol.model.dto.LiveAssistanceEventDto;
import com.prd.seccontrol.model.dto.UnitMonitoringStatusDto;
import com.prd.seccontrol.model.dto.UnitMonitoringStatusResume;
import com.prd.seccontrol.model.entity.ContractUnity;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssistanceEventRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.ScheduleMonthlyRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

  @Autowired
  private ScheduleMonthlyRepository scheduleMonthlyRepository;

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private GuardAssistanceEventRepository guardAssistanceEventRepository;

  public List<UnitMonitoringStatusDto> getUnitMonitoringStatus(LocalDate date) {
    Month month = date.getMonth();
    Integer year = date.getYear();
    LocalDateTime now = LocalDateTime.now();
    ScheduleMonthly currentSchedule = scheduleMonthlyRepository.findByMonthAndYear(month, year)
        .orElseThrow(() -> new RuntimeException("No schedule found for the given month and year"));

    List<UnitMonitoringStatusResume> assignmentCountDTOS = guardUnityScheduleAssignmentRepository
        .getAssignmentCountByScheduleMonthlyId(currentSchedule.getId(), now, date);

    return assignmentCountDTOS.stream()
        .map(resume -> new UnitMonitoringStatusDto(
            resume.getContractUnityId(),
            resume.getUnityName(),
            resume.getUnityCode(),
            resume.getClientName(),
            resume.getClientContractName(),
            resume.getAddress(),
            resume.getLatitude(),
            resume.getLongitude(),
            resume.getDate(),
            resume.getTotalShifts(),
            resume.getArrivedGuards(),
            resume.getAbsentGuards()
        ))
        .toList();
  }

  public List<GuardShiftDetailDto> getUnitShiftDetails(Long contractUnityId, LocalDate date) {
    Month month = date.getMonth();
    Integer year = date.getYear();
    LocalDateTime now = LocalDateTime.now();
    ScheduleMonthly currentSchedule = scheduleMonthlyRepository.findByMonthAndYear(month, year)
        .orElseThrow(() -> new RuntimeException("No schedule found for the given month and year"));

    List<GuardShiftBaseDTO> shiftDetails = dateGuardUnityAssignmentRepository
        .getShiftsBase(contractUnityId, currentSchedule.getId(), now, date);

    List<Long> dateGuardUnityAssignmentIds = shiftDetails.stream()
        .filter(GuardShiftBaseDTO::isHasMarks)
        .map(GuardShiftBaseDTO::getDateGuardUnityAssignmentId)
        .toList();

    List<GuardAssistanceEvent> assistanceEvents = guardAssistanceEventRepository
        .findLastEventsByAssignmentIds(dateGuardUnityAssignmentIds);

    return shiftDetails.stream()
        .map(this::mapToDetail)
        .peek(detail -> {
          if (detail.isHasArrived()) {
            assistanceEvents.stream()
                .filter(event -> event.getDateGuardUnityAssignmentId()
                    .equals(detail.getDateGuardUnityAssignmentId()))
                .findFirst()
                .ifPresent(event -> {
                  detail.setLastAssistanceType(event.getAssistanceType());
                  detail.setLastMarkTime(
                      LocalDateTime.of(event.getMarkDate(), event.getMarkTime()));
                });
          }
        })
        .toList();
  }

  public List<LiveAssistanceEventDto> getLiveAssists(LocalDate date) {

    // todo aligerar esto
    List<GuardAssistanceEvent> events = guardAssistanceEventRepository.findTop20ByOrderByCreatedAtDesc();

    List<LiveAssistanceEventDto> liveAssistanceEvents = new ArrayList<>();

    events.forEach(event -> {
      GuardUnityScheduleAssignment assignment = event.getDateGuardUnityAssignment()
          .getGuardUnityScheduleAssignment();
      ContractUnity contractUnity = assignment.getContractUnity();
      GuardAssignment guardAssignment = assignment.getGuardAssignment();
      String guardName = null;
      String guardCode = null;
      String guardDocumentNumber = null;
      if (guardAssignment.getGuard() != null) {
        guardName = guardAssignment.getGuard().getEmployee().getFirstName();
        guardCode = guardAssignment.getGuard().getCode();
        guardDocumentNumber = guardAssignment.getGuard().getEmployee().getDocumentNumber();
      }

      if (guardAssignment.getExternalGuard() != null) {
        guardName = guardAssignment.getExternalGuard().getFirstName();
        guardDocumentNumber = guardAssignment.getExternalGuard().getDocumentNumber();
      }

      LiveAssistanceEventDto liveAssistanceEventDto = new LiveAssistanceEventDto(
          event.getId(),
          event.getDateGuardUnityAssignmentId(),
          event.getGuardAssignmentId(),
          guardName,
          guardCode,
          guardDocumentNumber,
          contractUnity.getId(),
          contractUnity.getUnity().getName(),
          contractUnity.getClientContract().getClient().getName(),
          event.getPhotoUrl(),
          event.getLatitude(),
          event.getLongitude(),
          event.getMarkDate(),
          event.getMarkTime(),
          event.getSystemMark(),
          event.getAssistanceType(),
          event.getAssistanceProblemType(),
          event.getToleranceMinutes(),
          assignment.getGuardType(),
          event.getNumberOrder());

      liveAssistanceEvents.add(liveAssistanceEventDto);
    });

    return liveAssistanceEvents;
  }


  public GuardShiftDetailDto mapToDetail(GuardShiftBaseDTO base) {
    LocalDateTime now = LocalDateTime.now();

    boolean isFutureShift = now.isBefore(base.getTurnStartTime());
    boolean shouldBeOnPost = base.isHasMarks();

    boolean isAbsent =
        base.getScheduleAssignmentType() == ScheduleAssignmentType.ABSENT || (
            now.isAfter(base.getTurnStartTime()) &&
                now.isBefore(base.getTurnEndTime()) && !shouldBeOnPost);

    return new GuardShiftDetailDto(
        base.getDateGuardUnityAssignmentId(),
        base.getGuardName(),
        base.getGuardCode(),
        base.getDocumentNumber(),
        base.getGuardType(),
        base.getScheduleAssignmentType(),
        base.getTurnStartTime(),
        base.getTurnEndTime(),
        base.isException(),
        base.isHasExtraHours(),
        shouldBeOnPost,
        isAbsent,
        base.isHasMarks(),
        isFutureShift,
        null, // set after
        null // set after
    );
  }
}
