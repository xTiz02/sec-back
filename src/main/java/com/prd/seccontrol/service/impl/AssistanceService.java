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
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    Set<Long> last31Shifts = dateGuardUnityAssignmentRepository.findLastDateGuardUnityAssignmentIds(
        guard != null ? guard.getId() : null, externalGuard != null ? externalGuard.getId() : null,
        today, PageRequest.of(0, 10)
    ).stream().collect(Collectors.toSet());

    Set<Long> shiftsWithExitEvent = dateGuardUnityAssignmentRepository.findDateGuardIdsWithExitWithoutOpenExtraHours(
        last31Shifts);

    Set<Long> shiftsWithOutExitEvent = last31Shifts.stream()
        .filter(id -> !shiftsWithExitEvent.contains(id))
        .collect(Collectors.toSet());

    boolean useShiftsWithExitEvent = shiftsWithOutExitEvent.isEmpty();

    List<DateGuardUnityAssignmentInfo> dateGuardUnityAssignmentInfos =
        dateGuardUnityAssignmentRepository.findAllDateGuardUnityAssignmentInfo(
            useShiftsWithExitEvent ? shiftsWithExitEvent : shiftsWithOutExitEvent);

    if (dateGuardUnityAssignmentInfos.isEmpty()) {
      return new GuardCurrentShiftDto();
    }

    List<DateGuardUnityAssignmentInfo> dateGuardUnityAssignmentInfosWithExceptions = dateGuardUnityAssignmentInfos.stream()
        .filter(DateGuardUnityAssignmentInfo::hasException).toList();

    if (!dateGuardUnityAssignmentInfosWithExceptions.isEmpty()) {
      List<ScheduleException> exceptions = scheduleExceptionRepository.findByDateGuardUnityAssignmentIdIn(
          dateGuardUnityAssignmentInfosWithExceptions.stream()
              .map(DateGuardUnityAssignmentInfo::dateGuardUnityAssignmentId).toList());

      dateGuardUnityAssignmentInfos = setExceptionDateGuardFields(dateGuardUnityAssignmentInfos, exceptions);
    }

    DateGuardUnityAssignmentInfo currentShift = dateGuardUnityAssignmentInfos.stream()
        .min((a, b) -> {
          LocalDateTime aStart = turnTemplateService.getShiftDateTimeRange(a.date(),
              a.turnTemplate())[0];
          LocalDateTime bStart = turnTemplateService.getShiftDateTimeRange(b.date(),
              b.turnTemplate())[0];
          return useShiftsWithExitEvent ? bStart.compareTo(aStart) : aStart.compareTo(bStart);
        }).orElse(null);

    List<GuardAssistanceEventDto> assistanceEvents = guardAssistanceEventRepository.findByDateGuardUnityAssignmentId(
            currentShift.dateGuardUnityAssignmentId())
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
    if (exitEvent != null) {
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

    List<GuardRequestDto> lateRequests = guardRequestRepository.findByGuardAssistanceEventIdIn(
            assistanceEventIds)
        .stream()
        .map(lr -> {
          GuardAssistanceEventDto eventDto = assistanceEvents.stream()
              .filter(e -> e.id().equals(lr.getGuardAssistanceEventId()))
              .findFirst()
              .orElse(null);
          return new GuardRequestDto(lr, eventDto.assistanceType());
        })
        .toList();

    GuardCurrentShiftDto currentShiftDto = new GuardCurrentShiftDto(
        currentShift,
        assistanceEvents,
        activeExtraHours != null ? new GuardExtraHoursDto(activeExtraHours, coveredGuardName) : null,
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

    if(dateGuardUnityAssignmentInfo.hasException()) {
      List<ScheduleException> exceptions = scheduleExceptionRepository.findByDateGuardUnityAssignmentId(
          dateGuardUnityAssignmentInfo.dateGuardUnityAssignmentId());
      if(!exceptions.isEmpty()) {
        dateGuardUnityAssignmentInfo = setExceptionDateGuardFields(List.of(dateGuardUnityAssignmentInfo), exceptions).get(0);
      }

    }
    LocalDateTime[] shiftDateTimes = turnTemplateService.getShiftDateTimeRange(today,
        dateGuardUnityAssignmentInfo.turnTemplate());
    LocalDateTime shiftStartDateTime = shiftDateTimes[0];
    LocalDateTime shiftEndDateTime = shiftDateTimes[1];
    LocalDateTime nowDateTime = LocalDateTime.of(today, todayTime);

    boolean isLate = false;
    int differenceInMinutes = 0;
    int orderNumber = 0;
    if (request.assistanceType().equals(AssistanceType.ENTRY)) {
      //if is after of shift start time, is late
      orderNumber = 0;
      isLate = nowDateTime.isAfter(shiftStartDateTime);
      if (isLate) {
        differenceInMinutes = (int) Duration.between(shiftStartDateTime, nowDateTime).toMinutes();
      } else {
        // mark only 15 min before shift start time
        if (nowDateTime.isBefore(shiftStartDateTime.minusMinutes(15))) {
          throw new RuntimeException(
              "You can only mark assistance 15 minutes before shift start time");
        }
        differenceInMinutes = (int) Duration.between(nowDateTime, shiftStartDateTime).toMinutes();
      }
    }

    if (request.assistanceType().equals(AssistanceType.BREAK_START)) {
      orderNumber = 1;
      differenceInMinutes = (int) Duration.between(nowDateTime, shiftStartDateTime).toMinutes();
    }

    if (request.assistanceType().equals(AssistanceType.BREAK_END)) {
      GuardAssistanceEvent breakStartEvent = guardAssistanceEventRepository.findTopByDateGuardUnityAssignmentIdAndAssistanceType(
              request.dateGuardUnityAssignmentId(), AssistanceType.BREAK_START)
          .orElseThrow(() -> new RuntimeException("Break start event not found"));
      LocalDateTime startBreakDateTime = LocalDateTime.of(breakStartEvent.getMarkDate(),
          breakStartEvent.getMarkTime());
      orderNumber = 2;
      //if mark is after 1 hour 15 min of shift start time, is late
      isLate = nowDateTime.isAfter(startBreakDateTime.plusMinutes(75));
      if (isLate) {
        differenceInMinutes = (int) Duration.between(startBreakDateTime.plusMinutes(75),
            nowDateTime).toMinutes();
      } else {
        differenceInMinutes = (int) Duration.between(nowDateTime,
            startBreakDateTime.plusMinutes(75)).toMinutes();
      }
    }

    if (request.assistanceType().equals(AssistanceType.EXIT)) {
      dateGuardUnityAssignmentRepository.updateFinalizedByIds(true, List.of(request.dateGuardUnityAssignmentId()));
      orderNumber = 3;
      //if mark is after shift end time plus 15 min, is late
      isLate = nowDateTime.isAfter(shiftEndDateTime.plusMinutes(15));
      if (isLate) {
        differenceInMinutes = (int) Duration.between(shiftEndDateTime.plusMinutes(15), nowDateTime)
            .toMinutes();
      } else {
        differenceInMinutes = (int) Duration.between(nowDateTime, shiftEndDateTime.plusMinutes(15))
            .toMinutes();
      }
    }

    GuardAssistanceEvent event = new GuardAssistanceEvent();
    event.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setGuardType(dateGuardUnityAssignmentInfo.guardType());
    event.setGuardAssignmentId(dateGuardUnityAssignmentInfo.guardAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setAssistanceProblemType(
        isLate ? AssistanceProblemType.LATE : AssistanceProblemType.EARLY);
    event.setDifferenceInMinutes(differenceInMinutes);
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

  public List<DateGuardUnityAssignmentInfo> setExceptionDateGuardFields(
      List<DateGuardUnityAssignmentInfo> infos,
      List<ScheduleException> exceptions) {

    return infos.stream().map(info -> {
      ScheduleException exception = exceptions.stream()
          .filter(e -> e.getDateGuardUnityAssignmentId()
              .equals(info.dateGuardUnityAssignmentId()))
          .findFirst()
          .orElse(null);

      if (exception != null) {
        Guard eguard = exception.getGuardUnityScheduleAssignment()
            .getGuardAssignment().getGuard();

        ExternalGuard exguard = exception.getGuardUnityScheduleAssignment()
            .getGuardAssignment().getExternalGuard();

        if (eguard != null) {
          return new DateGuardUnityAssignmentInfo(
              eguard.getEmployee().getFirstName(),
              eguard.getEmployee().getDocumentNumber(),
              eguard.getPhotoUrl(),
              eguard.getGuardType(),
              exception.getGuardUnityScheduleAssignment().getGuardAssignment().getId(),
              exception.getGuardUnityScheduleAssignment().getId(),
              info
          );
        } else if (exguard != null) {
          return new DateGuardUnityAssignmentInfo(
              exguard.getFirstName(),
              exguard.getDocumentNumber(),
              null,
              exception.getGuardUnityScheduleAssignment().getGuardType(),
              exception.getGuardUnityScheduleAssignment().getGuardAssignment().getId(),
              exception.getGuardUnityScheduleAssignment().getId(),
              info
          );
        }
      }

      return info;
    }).toList();
  }


}
