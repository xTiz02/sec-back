package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.GuardLiteProjection;
import com.prd.seccontrol.model.dto.GuardLiteView;
import com.prd.seccontrol.model.dto.JustificationDto;
import com.prd.seccontrol.model.dto.ShiftAssistanceEventDetailDto;
import com.prd.seccontrol.model.dto.ShiftDetailDto;
import com.prd.seccontrol.model.dto.ShiftExceptionDetailDto;
import com.prd.seccontrol.model.dto.ShiftExtraHoursDetailDto;
import com.prd.seccontrol.model.dto.ShiftProjection;
import com.prd.seccontrol.model.dto.ShiftSearchParams;
import com.prd.seccontrol.model.dto.ShiftSearchResultDto;
import com.prd.seccontrol.model.dto.UnityLiteProjection;
import com.prd.seccontrol.model.dto.UnityLiteView;
import com.prd.seccontrol.model.entity.ContractUnity;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.Employee;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.entity.GuardExtraHours;
import com.prd.seccontrol.model.entity.GuardRequest;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.AssistanceProblemType;
import com.prd.seccontrol.model.types.IdentificationType;
import com.prd.seccontrol.repository.ClientRepository;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.EmployeeRepository;
import com.prd.seccontrol.repository.ExternalGuardRepository;
import com.prd.seccontrol.repository.GuardAssistanceEventRepository;
import com.prd.seccontrol.repository.GuardExtraHoursRepository;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.repository.GuardRequestRepository;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import com.prd.seccontrol.repository.SpecialServiceUnityRepository;
import com.prd.seccontrol.repository.UnityRepository;
import com.prd.seccontrol.util.SEConstants;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ShiftFilterService {

  @Autowired
  private GuardRepository guardRepository;

  @Autowired
  private ExternalGuardRepository externalGuardRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private UnityRepository unityRepository;

  @Autowired
  private SpecialServiceUnityRepository specialServiceUnityRepository;

  @Autowired
  private GuardAssistanceEventRepository guardAssistanceEventRepository;

  @Autowired
  private GuardRequestRepository guardRequestRepository;

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  @Autowired
  private GuardExtraHoursRepository guardExtraHoursRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  public Page<GuardLiteView> findGeneralGuards(String term, Pageable pageable) {
    Page<GuardLiteProjection> internalGuards = guardRepository.findAllUnified(term, pageable);

    return internalGuards
        .map(GuardLiteView::toDto);

  }

  public Page<UnityLiteView> findGeneralUnities(String term, Pageable pageable) {
    Page<UnityLiteProjection> unitiesPge = unityRepository.findAllUnified(term, pageable);

    return unitiesPge
        .map(UnityLiteView::toDto);
  }


  public Page<ShiftSearchResultDto> getDateGuardShiftsBYParam(ShiftSearchParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());
    Page<ShiftProjection> shifts = dateGuardUnityAssignmentRepository.searchShifts(
        params.dateFrom(),
        params.dateTo(),
        params.clientId(),
        params.guardId(),
        params.externalGuardId(),
        params.unityId(),
        params.specialServiceUnityId(),
        params.scheduleAssignmentType() != null ? params.scheduleAssignmentType().name() : null,
        params.hasExceptions(),
        params.hasExtraHours(),
        params.hasMarks(),
        pageable);

    List<Long> shiftsIds = shifts.map(ShiftProjection::getId).toList();

    List<GuardAssistanceEvent> events = guardAssistanceEventRepository.findByDateGuardUnityAssignmentIdIn(
        shiftsIds);

    Page<ShiftSearchResultDto> results = shifts.map(s -> {
      ShiftSearchResultDto finalShift = new ShiftSearchResultDto(s);

      List<GuardAssistanceEvent> eventsForShift = events.stream()
          .filter(e -> e.getDateGuardUnityAssignment().getId().equals(s.getId()))
          .toList();

      if (!eventsForShift.isEmpty()) {
        for (GuardAssistanceEvent event : eventsForShift) {
          switch (event.getAssistanceType()) {
            case ENTRY -> finalShift.setMarkEntry(event.getMarkTime());
            case BREAK_START -> finalShift.setMarkBreakStart(event.getMarkTime());
            case BREAK_END -> finalShift.setMarkBreakEnd(event.getMarkTime());
            case EXIT -> finalShift.setMarkExit(event.getMarkTime());
          }
        }
      }

      return finalShift;
    });

    return results;
  }

  public ShiftDetailDto getShiftDetailById(Long dateGuardUnityAssignmentId) {
    DateGuardUnityAssignment assignment = dateGuardUnityAssignmentRepository.findById(
        dateGuardUnityAssignmentId).orElseThrow(() -> new RuntimeException("Shift not found"));
    GuardUnityScheduleAssignment scheduleAssignment = assignment.getGuardUnityScheduleAssignment();
    String guardName = null;
    String guardCode = null;
    String documentNumber = null;
    String guardPhotoUrl = null;
    IdentificationType identificationType = null;

    if (scheduleAssignment.getGuardAssignment().getGuard() != null) {
      Guard scheduledGuard = scheduleAssignment.getGuardAssignment().getGuard();
      guardName = scheduledGuard.getEmployee().getFirstName() + " " + scheduledGuard
          .getEmployee().getLastName();
      guardCode = scheduledGuard.getCode();
      documentNumber = scheduledGuard.getEmployee().getDocumentNumber();
      guardPhotoUrl = scheduledGuard.getPhotoUrl();
      identificationType = scheduledGuard.getEmployee().getIdentificationType();
    } else if (scheduleAssignment.getGuardAssignment().getExternalGuard() != null) {
      ExternalGuard scheduledExternalGuard = scheduleAssignment.getGuardAssignment()
          .getExternalGuard();
      guardName = scheduledExternalGuard.getFirstName() + " " + scheduledExternalGuard
          .getLastName();
      documentNumber = scheduledExternalGuard.getDocumentNumber();
      identificationType = scheduledExternalGuard.getIdentificationType();
    }

    TurnTemplate turnTemplate = assignment.getTurnAndHour().getTurnTemplate();

    ContractUnity contractUnity = scheduleAssignment.getContractUnity();
    SpecialServiceUnitySchedule specialServiceUnitySchedule = scheduleAssignment.getSpecialServiceUnitySchedule();
    ShiftDetailDto shiftDetailDto = new ShiftDetailDto();
    shiftDetailDto.setId(assignment.getId());
    shiftDetailDto.setDate(assignment.getDate());
    shiftDetailDto.setScheduleAssignmentType(assignment.getScheduleAssignmentType());
    shiftDetailDto.setHasVacation(assignment.isHasVacation());
    shiftDetailDto.setHasExceptions(assignment.isHasExceptions());
    shiftDetailDto.setHasExtraHours(assignment.isHasExtraHours());
    shiftDetailDto.setHasMarks(assignment.isHasMarks());
    shiftDetailDto.setFinalized(assignment.isFinalized());

    shiftDetailDto.setGuardName(guardName);
    shiftDetailDto.setGuardCode(guardCode);
    shiftDetailDto.setDocumentNumber(documentNumber);
    shiftDetailDto.setGuardType(scheduleAssignment.getGuardType());
    shiftDetailDto.setExternalGuard(
        scheduleAssignment.getGuardAssignment().getExternalGuard() != null);
    shiftDetailDto.setGuardPhotoUrl(guardPhotoUrl);
    shiftDetailDto.setIdentificationType(identificationType);

    shiftDetailDto.setTurnName(turnTemplate.getName());
    shiftDetailDto.setTurnType(turnTemplate.getTurnType());
    shiftDetailDto.setScheduledEntry(assignment.getDateTimeEntry());
    shiftDetailDto.setScheduledExit(assignment.getDateTimeEnd());
    shiftDetailDto.setMaxScheduledEntry(assignment.getDateTimeEntry().plusMinutes(SEConstants.ENTRY_TOLERANCE));
    shiftDetailDto.setMaxScheduledExit(assignment.getDateTimeEnd().plusMinutes(SEConstants.EXIT_AVAILABLE_TIME));

    if (contractUnity != null) {
      shiftDetailDto.setContractUnityId(contractUnity.getId());
      shiftDetailDto.setContractUnityName(contractUnity.getUnity().getName());
      shiftDetailDto.setContractUnityCode(contractUnity.getUnity().getCode());
      shiftDetailDto.setUnityAddress(contractUnity.getUnity().getDirection());
      shiftDetailDto.setClientName(contractUnity.getClientContract().getClient().getName());
      shiftDetailDto.setClientContractName(contractUnity.getClientContract().getName());
    }
    if (specialServiceUnitySchedule != null) {
      shiftDetailDto.setSpecialServiceUnityId(
          specialServiceUnitySchedule.getSpecialServiceUnity().getId());
      shiftDetailDto.setSpecialServiceUnityName(
          specialServiceUnitySchedule.getSpecialServiceUnity().getUnityName());
      shiftDetailDto.setUnityAddress(
          specialServiceUnitySchedule.getSpecialServiceUnity().getAddress());
    }

    if (scheduleAssignment.getScheduleMonthly() != null) {
      shiftDetailDto.setScheduleMonthlyId(scheduleAssignment.getScheduleMonthly().getId());
      shiftDetailDto.setScheduleMonthlyName(scheduleAssignment.getScheduleMonthly().getName());
    }

    if(assignment.isHasMarks()) {
      List<GuardAssistanceEvent> eventsForShift = guardAssistanceEventRepository.findByDateGuardUnityAssignmentId(
          assignment.getId());
      List<ShiftAssistanceEventDetailDto>  assistanceEvents = eventsForShift.stream().map(e -> {
        ShiftAssistanceEventDetailDto eventDto = new ShiftAssistanceEventDetailDto();
        eventDto.setId(e.getId());
        eventDto.setAssistanceType(e.getAssistanceType());
        eventDto.setMarkDate(e.getMarkDate());
        eventDto.setMarkTime(e.getMarkTime());
        eventDto.setSystemMark(e.getSystemMark());
        eventDto.setLimitTimeToMark(e.getLimitTimeToMark());
        eventDto.setToleranceMinutes(e.getToleranceMinutes());

        if (e.getLimitTimeToMark() != null) {
          long differenceInMinutes = 0L;
          if(e.getMarkDate() != null && e.getMarkTime() != null){
            differenceInMinutes = Duration.between(e.getLimitTimeToMark(),
                LocalDateTime.of(e.getMarkDate(),e.getMarkTime())).toMinutes();
          } else if (e.getSystemMark() != null) {
            differenceInMinutes = Duration.between(e.getLimitTimeToMark(),
                e.getSystemMark()).toMinutes();
          }
          eventDto.setDifferenceInMinutes(differenceInMinutes);
        }

        eventDto.setNumberOrder(e.getNumberOrder());
        eventDto.setPhotoUrl(e.getPhotoUrl());
        eventDto.setLatitude(e.getLatitude());
        eventDto.setLongitude(e.getLongitude());
        eventDto.setIpAddress(e.getIpAddress());

        if (e.getAssistanceProblemType().equals(AssistanceProblemType.LATE)
            || e.getAssistanceProblemType().equals(AssistanceProblemType.LATE_JUSTIFIED)) {
          GuardRequest guardRequest = guardRequestRepository.findByGuardAssistanceEventId(
              e.getId()).orElse(null);
          if (guardRequest != null) {
            JustificationDto justificationDto = new JustificationDto();
            justificationDto.setId(guardRequest.getId());
            justificationDto.setDescription(guardRequest.getDescription());
            justificationDto.setRequestStatus(
                guardRequest.getRequestStatus());
            justificationDto.setCreatedAt(guardRequest.getCreatedAt());
            eventDto.setJustification(justificationDto);
          }
        }
        return eventDto;
      }).toList();

      shiftDetailDto.setAssistanceEvents(assistanceEvents);
    }


    if(assignment.isHasExceptions()) {
      ScheduleException scheduleException = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(
          assignment.getId()).stream().findFirst().orElse(null);
      if(scheduleException != null) {
        ShiftExceptionDetailDto exceptionDetailDto = new ShiftExceptionDetailDto();
        exceptionDetailDto.setId(scheduleException.getId());
        exceptionDetailDto.setScheduleExceptionType(scheduleException.getScheduleExceptionType());
        exceptionDetailDto.setDescription(scheduleException.getDescription());

        GuardUnityScheduleAssignment guardUnityScheduleAssignment = scheduleException.getGuardUnityScheduleAssignment();
        if(guardUnityScheduleAssignment.getGuardAssignment().getGuard() != null) {
          Guard replacementGuard = guardUnityScheduleAssignment.getGuardAssignment().getGuard();
          exceptionDetailDto.setReplacementGuardName(replacementGuard.getEmployee().getFirstName() + " " + replacementGuard.getEmployee().getLastName());
          exceptionDetailDto.setReplacementGuardCode(replacementGuard.getCode());
          exceptionDetailDto.setReplacementGuardType(guardUnityScheduleAssignment.getGuardType());
          exceptionDetailDto.setReplacementDocumentNumber(replacementGuard.getEmployee().getDocumentNumber());
        } else if (guardUnityScheduleAssignment.getGuardAssignment().getExternalGuard() != null) {
          ExternalGuard replacementExternalGuard = guardUnityScheduleAssignment.getGuardAssignment().getExternalGuard();
          exceptionDetailDto.setReplacementGuardName(replacementExternalGuard.getFirstName() + " " + replacementExternalGuard.getLastName());
          exceptionDetailDto.setReplacementGuardCode(null);
          exceptionDetailDto.setReplacementGuardType(guardUnityScheduleAssignment.getGuardType());
          exceptionDetailDto.setReplacementDocumentNumber(replacementExternalGuard.getDocumentNumber());
        }

        shiftDetailDto.setExceptions(List.of(exceptionDetailDto));
      }
    }

    if(assignment.isHasExtraHours()) {
      GuardExtraHours guardExtraHours = guardExtraHoursRepository.findByPrincipalDateGuardUnityAssignmentId(
          assignment.getId()).stream().findFirst().orElse(null);
      if(guardExtraHours != null) {
        ShiftExtraHoursDetailDto extraHoursDetailDto = new ShiftExtraHoursDetailDto();
        extraHoursDetailDto.setId(guardExtraHours.getId());
        extraHoursDetailDto.setStartDate(guardExtraHours.getStartDate());
        extraHoursDetailDto.setStartTime(guardExtraHours.getStartTime());
        extraHoursDetailDto.setEndDate(guardExtraHours.getEndDate());
        extraHoursDetailDto.setEndTime(guardExtraHours.getEndTime());
        extraHoursDetailDto.setExtraHours(guardExtraHours.getExtraHours());
        extraHoursDetailDto.setPrincipalGuardName(shiftDetailDto.getGuardName());
        extraHoursDetailDto.setPrincipalShiftDate(shiftDetailDto.getDate());

        DateGuardUnityAssignment coverAssignment = guardExtraHours.getCoverDateGuardUnityAssignment();
        if(coverAssignment != null) {
          GuardUnityScheduleAssignment coverScheduleAssignment = coverAssignment.getGuardUnityScheduleAssignment();
          if(coverScheduleAssignment.getGuardAssignment().getGuard() != null) {
            Guard coverGuard = coverScheduleAssignment.getGuardAssignment().getGuard();
            extraHoursDetailDto.setCoverGuardName(coverGuard.getEmployee().getFirstName() + " " + coverGuard.getEmployee().getLastName());
            extraHoursDetailDto.setCoverShiftDate(coverAssignment.getDate());
          } else if (coverScheduleAssignment.getGuardAssignment().getExternalGuard() != null) {
            ExternalGuard coverExternalGuard = coverScheduleAssignment.getGuardAssignment().getExternalGuard();
            extraHoursDetailDto.setCoverGuardName(coverExternalGuard.getFirstName() + " " + coverExternalGuard.getLastName());
            extraHoursDetailDto.setCoverShiftDate(coverAssignment.getDate());
          }
        }

        Employee operator = employeeRepository.findByUserId(guardExtraHours.getOperatorUserId()).orElse(null);
        if(operator != null) {
          extraHoursDetailDto.setOperatorUserName(operator.getFirstName() + " " + operator.getLastName());
        } else {
          extraHoursDetailDto.setOperatorUserName("Usuario no encontrado");
        }
        extraHoursDetailDto.setCreatedAt(guardExtraHours.getCreatedAt());

        shiftDetailDto.setExtraHours(List.of(extraHoursDetailDto));
      }
    }

    return shiftDetailDto;
  }


}
