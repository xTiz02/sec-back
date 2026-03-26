package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateAssistanceEventRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
import com.prd.seccontrol.model.dto.GuardAssistanceEventDto;
import com.prd.seccontrol.model.dto.GuardCurrentShiftDto;
import com.prd.seccontrol.model.dto.GuardExtraHoursDto;
import com.prd.seccontrol.model.dto.GuardRequestDto;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.entity.GuardExtraHours;
import com.prd.seccontrol.model.entity.ScheduleException;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.model.types.AssistanceProblemType;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.ExternalGuardRepository;
import com.prd.seccontrol.repository.GuardAssistanceEventRepository;
import com.prd.seccontrol.repository.GuardExtraHoursRepository;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.repository.GuardRequestRepository;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import com.prd.seccontrol.repository.UserRepository;
import com.prd.seccontrol.util.SEConstants;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistanceService {

  @Autowired
  private GuardAssistanceEventRepository guardAssistanceEventRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private GuardRequestRepository guardRequestRepository;

  @Autowired
  private GuardExtraHoursRepository guardExtraHoursRepository;

  @Autowired
  private GuardRepository guardRepository;

  @Autowired
  private ExternalGuardRepository externalGuardRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TurnTemplateService turnTemplateService;

  @Autowired
  private ScheduleExceptionRepository scheduleExceptionRepository;

  public GuardCurrentShiftDto getGuardCurrentShift(Principal principal) {
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();
    User user = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Guard guard = guardRepository.findByEmployee_UserId(user.getId())
        .orElse(null);

    ExternalGuard externalGuard = externalGuardRepository.findByUserId(user.getId())
        .orElse(null);

    List<Object[]> availableDatesGuards =
        dateGuardUnityAssignmentRepository.findLastDateGuardUnityAssignmentIds(
            guard != null ? guard.getId() : null,
            externalGuard != null ? externalGuard.getId() : null,
            today.minus(Duration.ofDays(SEConstants.INTERVAL_DAYS)),
            today.plus(Duration.ofDays(SEConstants.INTERVAL_DAYS))
        );

    if (availableDatesGuards.isEmpty()) {
      return new GuardCurrentShiftDto();
    }

    //order by date and dateFrom
    Object[] firts = availableDatesGuards.stream().min((a, b) -> {
          LocalDateTime[] dateTimesA = turnTemplateService.getShiftDateTimeRange((LocalDate) a[2],
              (TurnTemplate) a[1]);
          LocalDateTime[] dateTimesB = turnTemplateService.getShiftDateTimeRange((LocalDate) b[2],
              (TurnTemplate) b[1]);
          LocalDateTime startA = dateTimesA[0];
          LocalDateTime endA = dateTimesA[1];
          LocalDateTime startB = dateTimesB[0];
          LocalDateTime endB = dateTimesB[1];
          if (startA.isBefore(startB)) {
            return -1;
          } else if (startA.isAfter(startB)) {
            return 1;
          } else {
            if (endA.isBefore(endB)) {
              return -1;
            } else if (endA.isAfter(endB)) {
              return 1;
            } else {
              return 0;
            }
          }
        })
        .orElse(null);

    Long dateGuardUnityAssignmentId = (Long) firts[0];

    //get complete info of shift
    List<DateGuardUnityAssignmentInfo> dateGuardUnityAssignmentInfos =
        dateGuardUnityAssignmentRepository.findAllDateGuardUnityAssignmentInfo(
            List.of(dateGuardUnityAssignmentId));

    if (dateGuardUnityAssignmentInfos.isEmpty()) {
      return new GuardCurrentShiftDto();
    }

    DateGuardUnityAssignmentInfo sampleInfo = dateGuardUnityAssignmentInfos.get(0);
    boolean isException = sampleInfo.hasException();

    if (isException) {
      setExceptionDateGuardFields(guard, externalGuard, sampleInfo);
    }

    List<GuardAssistanceEventDto> assistanceEvents = guardAssistanceEventRepository.findByDateGuardUnityAssignmentId(
            sampleInfo.dateGuardUnityAssignmentId())
        .stream()
        .map(GuardAssistanceEventDto::new)
        .toList();

    List<Long> assistanceEventIds = assistanceEvents.stream().map(GuardAssistanceEventDto::id)
        .toList();

    GuardAssistanceEventDto exitEvent = assistanceEvents.stream()
        .filter(e -> e.assistanceType().equals(AssistanceType.EXIT))
        .findFirst()
        .orElse(null);

    GuardExtraHours activeExtraHours = null;

    String coveredGuardName = null;
    if (exitEvent != null && sampleInfo.hasExtraHours()) {
      activeExtraHours = guardExtraHoursRepository.findByGuardAssistanceEventId(exitEvent.id())
          .orElse(null);
      if (activeExtraHours != null) {
        GuardAssignment guardAssignment = activeExtraHours.getGuardAssignment();
        Guard cguard = guardAssignment.getGuard();
        ExternalGuard cexternalGuard = guardAssignment.getExternalGuard();
        coveredGuardName =
            cguard != null ? cguard.getEmployee().getFirstName() : cexternalGuard.getFirstName();
      }
    }

    boolean hasLateEvents = assistanceEvents.stream()
        .anyMatch(e -> e.assistanceProblemType() != null &&
            (e.assistanceProblemType().equals(AssistanceProblemType.LATE) ||
                e.assistanceProblemType().equals(AssistanceProblemType.LATE_JUSTIFIED)));
    List<GuardRequestDto> lateRequests =
        hasLateEvents ? guardRequestRepository.findByGuardAssistanceEventIdIn(
                assistanceEventIds)
            .stream()
            .map(lr -> {
              GuardAssistanceEventDto eventDto = assistanceEvents.stream()
                  .filter(e -> e.id().equals(lr.getGuardAssistanceEventId()))
                  .findFirst()
                  .orElse(null);
              return new GuardRequestDto(lr, eventDto.assistanceType());
            })
            .toList() : List.of();

    GuardCurrentShiftDto currentShiftDto = new GuardCurrentShiftDto(
        sampleInfo,
        assistanceEvents,
        activeExtraHours != null ? new GuardExtraHoursDto(activeExtraHours, coveredGuardName)
            : null,
        lateRequests);

    return currentShiftDto;
  }

  @Transactional
  public GuardAssistanceEventDto markAssist(CreateAssistanceEventRequest request,
      Principal principal) {
    LocalDate today = LocalDate.now();
    LocalTime todayTime = LocalTime.now();
    User user = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Guard guard = guardRepository.findByEmployee_UserId(user.getId())
        .orElse(null);

    ExternalGuard externalGuard = externalGuardRepository.findByUserId(user.getId())
        .orElse(null);

    if (guard == null && externalGuard == null) {
      throw new RuntimeException("Guard not found");
    }

    DateGuardUnityAssignmentInfo dateGuardUnityAssignmentInfo =
        dateGuardUnityAssignmentRepository.findDateGuardUnityAssignmentInfoById(
                request.dateGuardUnityAssignmentId())
            .orElseThrow(() -> new RuntimeException("Shift not found"));

    if (dateGuardUnityAssignmentInfo.hasException()) {
      List<ScheduleException> exceptions = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(
          dateGuardUnityAssignmentInfo.dateGuardUnityAssignmentId());
      if (!exceptions.isEmpty()) {
        setExceptionDateGuardFields(
            guard, externalGuard, dateGuardUnityAssignmentInfo);
      }

    }
    LocalDateTime[] shiftDateTimes = turnTemplateService.getShiftDateTimeRange(today,
        dateGuardUnityAssignmentInfo.turnTemplate());
    LocalDateTime shiftStartDateTime = shiftDateTimes[0];
    LocalDateTime shiftEndDateTime = shiftDateTimes[1];
    LocalDateTime markTime = LocalDateTime.of(today, todayTime);
    LocalDateTime limitTimeToMark = null;
    Long toleranceMinutes = 0L;
    boolean isLate = false;
    int orderNumber = 0;

    if (request.assistanceType().equals(AssistanceType.ENTRY)) {
      orderNumber = 0;
      toleranceMinutes = SEConstants.ENTRY_TOLERANCE;
      limitTimeToMark = shiftStartDateTime.plusMinutes(SEConstants.ENTRY_TOLERANCE);
      isLate = markTime.isAfter(limitTimeToMark);
      if (markTime.isBefore(shiftStartDateTime.minusMinutes(SEConstants.ENTRY_AVAILABLE_TIME))) {
        throw new RuntimeException(
            "Solo se puede marcar el ingreso " + SEConstants.ENTRY_AVAILABLE_TIME + " minutos antes del inicio del turno");
      }
    }

    if (request.assistanceType().equals(AssistanceType.BREAK_START)) {
      orderNumber = 1;
    }

    if (request.assistanceType().equals(AssistanceType.BREAK_END)) {
      orderNumber = 2;
      GuardAssistanceEvent breakStartEvent = guardAssistanceEventRepository.findTopByDateGuardUnityAssignmentIdAndAssistanceType(
              request.dateGuardUnityAssignmentId(), AssistanceType.BREAK_START)
          .orElseThrow(() -> new RuntimeException("No se encontró el evento de inicio de descanso para esta asignación"));
      LocalDateTime startBreakDateTime = LocalDateTime.of(breakStartEvent.getMarkDate(),
          breakStartEvent.getMarkTime());
      toleranceMinutes = SEConstants.BREAK_EXIT_TOLERANCE;
      limitTimeToMark = startBreakDateTime.plusMinutes(SEConstants.BREAK_TIME + SEConstants.BREAK_EXIT_TOLERANCE);
      isLate = markTime.isAfter(limitTimeToMark);
      if(markTime.isBefore(startBreakDateTime.plusMinutes(SEConstants.BREAK_TIME))) {
        throw new RuntimeException(
            "Solo se puede marcar el fin del descanso después de " + SEConstants.BREAK_TIME
                + " minutos desde el inicio del descanso");
      }
    }

    if (request.assistanceType().equals(AssistanceType.EXIT)) {
      orderNumber = 3;
      dateGuardUnityAssignmentRepository.updateFinalizedByIds(true,
          List.of(request.dateGuardUnityAssignmentId()));

      limitTimeToMark = shiftEndDateTime.plusMinutes(SEConstants.EXIT_AVAILABLE_TIME);
      isLate = markTime.isAfter(limitTimeToMark);
      toleranceMinutes = SEConstants.EXIT_AVAILABLE_TIME;
      if(markTime.isBefore(shiftEndDateTime)) {
        throw new RuntimeException(
            "Solo se puede marcar la salida "+ SEConstants.EXIT_AVAILABLE_TIME +" minutos después del fin del turno");
      }
    }

    GuardAssistanceEvent event = new GuardAssistanceEvent();
    event.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setGuardType(dateGuardUnityAssignmentInfo.guardType());
    event.setGuardAssignmentId(dateGuardUnityAssignmentInfo.guardAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setLimitTimeToMark(limitTimeToMark);
    event.setToleranceMinutes(toleranceMinutes);
    event.setAssistanceProblemType(
        isLate ? AssistanceProblemType.LATE : AssistanceProblemType.EARLY);
    event.setNumberOrder(orderNumber);
    event.setLongitude(request.longitude());
    event.setLatitude(request.latitude());
    event.setPhotoUrl(null); //TODO: save photo and set url
    event.setMarkDate(today);
    event.setMarkTime(todayTime);
    event.setSystemMark(null);

    event = guardAssistanceEventRepository.save(event);

    GuardAssistanceEventDto assistanceEventDto = new GuardAssistanceEventDto(event);

    return assistanceEventDto;
  }


  public DateGuardUnityAssignmentInfo setExceptionDateGuardFields(
      Guard guard, ExternalGuard externalGuard, DateGuardUnityAssignmentInfo info) {
    if (externalGuard != null) {
      return new DateGuardUnityAssignmentInfo(
          externalGuard.getFirstName(),
          externalGuard.getDocumentNumber(),
          null,
          info.guardType(),
          info.dateGuardUnityAssignmentId(),
          info.guardAssignmentId(),
          info
      );
    } else if (guard != null) {
      return new DateGuardUnityAssignmentInfo(
          guard.getEmployee().getFirstName(),
          guard.getEmployee().getDocumentNumber(),
          guard.getPhotoUrl(),
          guard.getGuardType(),
          info.dateGuardUnityAssignmentId(),
          info.guardAssignmentId(),
          info
      );
    } else {
      return info;
    }
  }


}
