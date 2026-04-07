package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateAssistanceEventRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
import com.prd.seccontrol.model.dto.GuardAssistanceEventDto;
import com.prd.seccontrol.model.dto.GuardCurrentShiftDto;
import com.prd.seccontrol.model.dto.GuardExtraHoursDto;
import com.prd.seccontrol.model.dto.GuardRequestDto;
import com.prd.seccontrol.model.dto.LiveAssistanceEventDto;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.entity.GuardExtraHours;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
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
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.ScheduleExceptionRepository;
import com.prd.seccontrol.repository.ScheduleMonthlyRepository;
import com.prd.seccontrol.repository.SpecialServiceUnityScheduleRepository;
import com.prd.seccontrol.repository.UserRepository;
import com.prd.seccontrol.service.inter.AwsS3Service;
import com.prd.seccontrol.util.SEConstants;
import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private SpecialServiceUnityScheduleRepository specialServiceUnityScheduleRepository;

  @Autowired
  private ScheduleMonthlyRepository scheduleMonthlyRepository;

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  private AwsS3Service awsS3Service;

  public GuardCurrentShiftDto getGuardCurrentShift(Principal principal) {
    LocalDate today = LocalDate.now();
    LocalTime now = LocalTime.now();
    Month currentMonth = today.getMonth();
    Integer currentYear = today.getYear();
    Month yesterdayMonth = today.minusDays(1).getMonth();
    Integer yesterdayYear = today.minusDays(1).getYear();
    User user = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Guard guard = guardRepository.findByEmployee_UserId(user.getId())
        .orElse(null);

    ExternalGuard externalGuard = externalGuardRepository.findByUserId(user.getId())
        .orElse(null);

    List<SpecialServiceUnitySchedule> activeSpecialSchedules = specialServiceUnityScheduleRepository.findByDateBetween(
        today);
    List<ScheduleMonthly> scheduleMonthlyList = scheduleMonthlyRepository.findByMonthAndYearAndYesterdayDay(
        currentMonth, currentYear, yesterdayMonth, yesterdayYear);

    List<Long> activeSpecialScheduleIds = activeSpecialSchedules.stream()
        .map(SpecialServiceUnitySchedule::getId).toList();
    List<Long> activeScheduleMonthlyIds = scheduleMonthlyList.stream().map(ScheduleMonthly::getId)
        .toList();

    List<GuardUnityScheduleAssignment> activeGuardUnityScheduleAssignments = guardUnityScheduleAssignmentRepository.
        findByGuardIdAndScheduleMonthlyIdsOrSpecialServiceUnityScheduleIds(
            activeSpecialScheduleIds.isEmpty() ? null : activeScheduleMonthlyIds,
            activeScheduleMonthlyIds.isEmpty() ? null : activeSpecialScheduleIds,
            guard != null ? guard.getId() : null,
            externalGuard != null ? externalGuard.getId() : null);

    List<Long> activeGuardUnityScheduleAssignmentIds = activeGuardUnityScheduleAssignments.stream()
        .map(GuardUnityScheduleAssignment::getId).toList();

    List<Object[]> availableDatesGuards =
        dateGuardUnityAssignmentRepository.findLastDateGuardUnityAssignmentIds(
            today.minusDays(SEConstants.INTERVAL_DAYS),
            today.plusDays(SEConstants.INTERVAL_DAYS),
            activeGuardUnityScheduleAssignmentIds
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

    // set TurnTemplate and GuardUnityAssignmentId to sampleInfo because they are needed to complete
    sampleInfo.setTurnTemplate((TurnTemplate) firts[1]);
    sampleInfo.setGuardUnityAssignmentId(firts[3] != null ? (Long) firts[3] : (Long) firts[4]);

    DateGuardUnityAssignmentInfo finalSampleInfo = sampleInfo;
    GuardUnityScheduleAssignment gusa = activeGuardUnityScheduleAssignments.stream()
        .filter(a -> a.getId().equals(finalSampleInfo.getGuardUnityAssignmentId()))
        .findFirst()
        .orElse(null);

    if (gusa == null) {
      return new GuardCurrentShiftDto();
    }
    sampleInfo = completeShiftSimpleInfo(gusa, sampleInfo);

//    boolean isException = completeInfo.hasException();
//
//    if (isException) {
//      setExceptionDateGuardFields(guard, externalGuard, completeInfo);
//    }

    List<GuardAssistanceEventDto> assistanceEvents = guardAssistanceEventRepository.findByDateGuardUnityAssignmentId(
            sampleInfo.getDateGuardUnityAssignmentId())
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

    //todo aligerar esto
    if (exitEvent != null && sampleInfo.isHasExtraHours()) {
      activeExtraHours = guardExtraHoursRepository.findByPrincipalDateGuardUnityAssignmentId(
              sampleInfo.getDateGuardUnityAssignmentId())
          .orElse(null);
      if (activeExtraHours != null) {
        if(activeExtraHours.getCoverDateGuardUnityAssignmentId() != null) {
          GuardAssignment guardAssignment = activeExtraHours.getCoverDateGuardUnityAssignment()
              .getGuardUnityScheduleAssignment()
              .getGuardAssignment();
          Guard cguard = guardAssignment.getGuard();
          ExternalGuard cexternalGuard = guardAssignment.getExternalGuard();
          coveredGuardName =
              cguard != null ? cguard.getEmployee().getFirstName() : cexternalGuard.getFirstName();
        }
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
        gusa,
        assistanceEvents,
        activeExtraHours != null ? new GuardExtraHoursDto(activeExtraHours, coveredGuardName)
            : null,
        lateRequests);

    return currentShiftDto;
  }

  @Transactional
  public GuardAssistanceEventDto markAssist(CreateAssistanceEventRequest request, Guard guard,
      ExternalGuard externalGuard,
      LocalDate today, LocalTime todayTime, boolean isSystem, Principal principal)
      throws IOException {

    DateGuardUnityAssignmentInfo dateGuardUnityAssignmentInfo =
        dateGuardUnityAssignmentRepository.findDateGuardUnityAssignmentInfoById(
                request.dateGuardUnityAssignmentId())
            .orElseThrow(() -> new RuntimeException("Shift not found"));

    // todo validar que el assistanceType tenga eventos anteriores si corresponde.
    if (dateGuardUnityAssignmentInfo.isHasException()) {
      GuardUnityScheduleAssignment seGusa = guardUnityScheduleAssignmentRepository.findById(
              dateGuardUnityAssignmentInfo.getSeGuardUnityAssignmentId())
          .orElseThrow(() -> new RuntimeException("GuardUnityScheduleAssignment not found"));

      dateGuardUnityAssignmentInfo = completeShiftSimpleInfo(seGusa, dateGuardUnityAssignmentInfo);
    } else {
      GuardUnityScheduleAssignment gusa = guardUnityScheduleAssignmentRepository.findById(
              dateGuardUnityAssignmentInfo.getGuardUnityAssignmentId())
          .orElseThrow(() -> new RuntimeException("GuardUnityScheduleAssignment not found"));
      dateGuardUnityAssignmentInfo = completeShiftSimpleInfo(gusa, dateGuardUnityAssignmentInfo);
    }

    String documentNumber =
        guard != null ? guard.getEmployee().getDocumentNumber() : externalGuard.getDocumentNumber();

    // todo validar para excepcion por el porceso de cerrado(no deja si hay excepcion)
    if (!isSystem && !dateGuardUnityAssignmentInfo.getGuardDocumentNumber().equals(documentNumber)) {
      throw new RuntimeException("No se puede marcar asistencia para esta asignación");
    }

    LocalDateTime[] shiftDateTimes = turnTemplateService.getShiftDateTimeRange(dateGuardUnityAssignmentInfo.getDate(),
        dateGuardUnityAssignmentInfo.getTurnTemplate());
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

      if (!isSystem && markTime.isBefore(
          shiftStartDateTime.minusMinutes(SEConstants.ENTRY_AVAILABLE_TIME))) {
        throw new RuntimeException(
            "Solo se puede marcar el ingreso " + SEConstants.ENTRY_AVAILABLE_TIME
                + " minutos antes del inicio del turno");
      }

    }

    if (request.assistanceType().equals(AssistanceType.BREAK_START)) {
      orderNumber = 1;
      limitTimeToMark = shiftEndDateTime.minusMinutes(
          SEConstants.BREAK_AUTO_CLOSE_TIME_BEFORE_EXIT + SEConstants.BREAK_TIME
              + SEConstants.BREAK_EXIT_TOLERANCE);
    }

    if (request.assistanceType().equals(AssistanceType.BREAK_END)) {
      orderNumber = 2;
      GuardAssistanceEvent breakStartEvent = guardAssistanceEventRepository.findTopByDateGuardUnityAssignmentIdAndAssistanceType(
              request.dateGuardUnityAssignmentId(), AssistanceType.BREAK_START)
          .orElseThrow(() -> new RuntimeException(
              "No se encontró el evento de inicio de descanso para esta asignación"));
      LocalDateTime startBreakDateTime = LocalDateTime.of(breakStartEvent.getMarkDate(),
          breakStartEvent.getMarkTime());
      toleranceMinutes = SEConstants.BREAK_EXIT_TOLERANCE;
      limitTimeToMark = startBreakDateTime.plusMinutes(
          SEConstants.BREAK_TIME + SEConstants.BREAK_EXIT_TOLERANCE);
      isLate = markTime.isAfter(limitTimeToMark);
      if (!isSystem && markTime.isBefore(startBreakDateTime.plusMinutes(SEConstants.BREAK_TIME))) {
        throw new RuntimeException(
            "Solo se puede marcar el fin del descanso después de " + SEConstants.BREAK_TIME
                + " minutos desde el inicio del descanso");
      }
    }

    if (request.assistanceType().equals(AssistanceType.EXIT)) {
      orderNumber = 3;

      limitTimeToMark = shiftEndDateTime.plusMinutes(SEConstants.EXIT_AVAILABLE_TIME);
      isLate = markTime.isAfter(limitTimeToMark);
      toleranceMinutes = SEConstants.EXIT_AVAILABLE_TIME;
      if (!isSystem && markTime.isBefore(shiftEndDateTime)) {
        throw new RuntimeException(
            "Solo se puede marcar la salida " + SEConstants.EXIT_AVAILABLE_TIME
                + " minutos después del fin del turno");
      }
    }

    GuardAssistanceEvent event = new GuardAssistanceEvent();
    event.setDateGuardUnityAssignmentId(request.dateGuardUnityAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setGuardType(dateGuardUnityAssignmentInfo.getGuardType());
    event.setGuardAssignmentId(dateGuardUnityAssignmentInfo.getGuardAssignmentId());
    event.setAssistanceType(request.assistanceType());
    event.setLimitTimeToMark(limitTimeToMark);
    event.setToleranceMinutes(toleranceMinutes);
    event.setAssistanceProblemType(
        isLate ? AssistanceProblemType.LATE : AssistanceProblemType.EARLY);
    event.setNumberOrder(orderNumber);
    event.setLongitude(request.longitude());
    event.setLatitude(request.latitude());
    event.setPhotoUrl(null);
    event.setMarkDate(isSystem ? null : today);
    event.setMarkTime(isSystem ? null : todayTime);
    event.setSystemMark(isSystem ? LocalDateTime.of(today, todayTime) : null);

    event = guardAssistanceEventRepository.save(event);

    if (event.getAssistanceType().equals(AssistanceType.ENTRY)) {
      dateGuardUnityAssignmentRepository.updateHasMarksById(true,
          request.dateGuardUnityAssignmentId());
      GuardExtraHours coverExtraHours = guardExtraHoursRepository.findByCoverDateGuardUnityAssignmentId(
              dateGuardUnityAssignmentInfo.getDateGuardUnityAssignmentId())
          .orElse(null);
      if (coverExtraHours != null) {
        coverExtraHours.setEndDate(today);
        coverExtraHours.setEndTime(todayTime);
        if(coverExtraHours.getStartDate() != null) {
          coverExtraHours.setExtraHours((int)
              Duration.between(LocalDateTime.of(coverExtraHours.getStartDate(), coverExtraHours.getStartTime()),
                  LocalDateTime.of(today, todayTime)).toMinutes());
        }
        guardExtraHoursRepository.save(coverExtraHours);
      }
    }
    if (event.getAssistanceType().equals(AssistanceType.EXIT)) {
      dateGuardUnityAssignmentRepository.updateFinalizedByIds(true,
          List.of(request.dateGuardUnityAssignmentId()));
    }

    GuardAssistanceEventDto assistanceEventDto = new GuardAssistanceEventDto(event);

    if (!isSystem) {
      LiveAssistanceEventDto liveAssistanceEventDto = new LiveAssistanceEventDto(
          event.getId(),
          event.getDateGuardUnityAssignmentId(),
          event.getGuardAssignmentId(),
          dateGuardUnityAssignmentInfo.getGuardName(),
          dateGuardUnityAssignmentInfo.getGuardCode(),
          dateGuardUnityAssignmentInfo.getGuardDocumentNumber(),
          dateGuardUnityAssignmentInfo.getContractUnityId(),
          dateGuardUnityAssignmentInfo.getUnityName(),
          dateGuardUnityAssignmentInfo.getClientName(),
          event.getPhotoUrl(),
          event.getLatitude(),
          event.getLongitude(),
          event.getMarkDate(),
          event.getMarkTime(),
          event.getSystemMark(),
          event.getAssistanceType(),
          event.getAssistanceProblemType(),
          event.getToleranceMinutes(),
          dateGuardUnityAssignmentInfo.getGuardType(),
          event.getNumberOrder());

      simpMessagingTemplate.convertAndSend("/topic/assists", liveAssistanceEventDto);
    }

    if (request.photo() != null && !request.photo().isEmpty()) {
      String path = "";
      if(request.photo().getOriginalFilename() == null){
        path = event.getId() + "-" + UUID.randomUUID()+ ".jpg";
      } else {
        path = event.getId() + "-" + UUID.randomUUID() + "-" + request.photo().getOriginalFilename();
      }
      System.out.println(path);
//      Boolean photoUrl = awsS3Service.uploadFile(path, request.photo());
//      if (photoUrl) {
//        String url = awsS3Service.getFileUrl(path);
//        event.setPhotoUrl(url);
//        guardAssistanceEventRepository.save(event);
//        assistanceEventDto = new GuardAssistanceEventDto(event);
//      }
    }

      return assistanceEventDto;
  }

  public DateGuardUnityAssignmentInfo completeShiftSimpleInfo(GuardUnityScheduleAssignment gusa,
      DateGuardUnityAssignmentInfo info) {
    String guardName = null;
    String guardDocumentNumber = null;
    String guardPhotoUrl = null;
    String guardCode = null;
    String clientName = null;
    if (gusa.getGuardAssignment().getGuard() != null) {
      Guard guard = gusa.getGuardAssignment().getGuard();
      guardName = guard.getEmployee().getFirstName();
      guardDocumentNumber = guard.getEmployee().getDocumentNumber();
      guardPhotoUrl = guard.getPhotoUrl();
      guardCode = guard.getCode();
    } else if (gusa.getGuardAssignment().getExternalGuardId() != null) {
      ExternalGuard externalGuard = gusa.getGuardAssignment().getExternalGuard();
      guardName = externalGuard.getFirstName();
      guardDocumentNumber = externalGuard.getDocumentNumber();
    }
    String unityName = null;
    String address = null;
    Double latitude = null;
    Double longitude = null;
    Double allowedRadius = null;

    if (gusa.getContractUnity() != null) {
      unityName = gusa.getContractUnity().getUnity().getName();
      address = gusa.getContractUnity().getUnity().getDirection();
      latitude = gusa.getContractUnity().getUnity().getLatitude();
      longitude = gusa.getContractUnity().getUnity().getLongitude();
      allowedRadius = gusa.getContractUnity().getUnity().getRangeCoverage();
      clientName = gusa.getContractUnity().getClientContract().getClient().getName();
    } else if (gusa.getSpecialServiceUnitySchedule() != null
        && gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity() != null) {
      unityName = gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity().getUnityName();
      address = gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity().getAddress();
      latitude = gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity().getLatitude();
      longitude = gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity().getLongitude();
      allowedRadius = gusa.getSpecialServiceUnitySchedule().getSpecialServiceUnity()
          .getRangeCoverage();
    }

    return new DateGuardUnityAssignmentInfo(
        guardName, guardDocumentNumber, guardPhotoUrl, gusa.getGuardType(),
        info.getDateGuardUnityAssignmentId(), gusa.getGuardAssignmentId(), gusa.getId(),
        info.getDate(), gusa.getContractUnityId(),
        gusa.getSpecialServiceUnitySchedule() != null ? gusa.getSpecialServiceUnitySchedule()
            .getSpecialServiceUnityId() : null, unityName, clientName, guardCode, address, latitude,
        longitude, allowedRadius,
        info.getTurnTemplate(), info.getTurnAndHourId(), info.isHasException(),
        info.isHasExtraHours(),
        info.isFinalized());
  }

}
