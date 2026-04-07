package com.prd.seccontrol.jobs;

import com.prd.seccontrol.model.dto.CreateAssistanceEventRequest;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssistanceEventRepository;
import com.prd.seccontrol.repository.GuardExtraHoursRepository;
import com.prd.seccontrol.service.impl.AssistanceService;
import com.prd.seccontrol.service.impl.DateGuardUnityAssignmentService;
import com.prd.seccontrol.util.SEConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseAssistanceProcess {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(
      CloseAssistanceProcess.class);

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private AssistanceService assistanceService;

  @Autowired
  private DateGuardUnityAssignmentService dateGuardUnityAssignmentService;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Autowired
  private GuardAssistanceEventRepository guardAssistanceEventRepository;

  @Autowired
  private GuardExtraHoursRepository guardExtraHoursRepository;

  // Todo Aligerar el proceso para que sea lo mas repido posible , evitar llamar a un DateGuardUnityAssignment completo.
  @Scheduled(fixedDelay = 10 * 60 * 1000) // 10 min después de que terminó la ejecución anterior
  public void closeAssistanceProcess() {
//    return;
    logger.info("Iniciando proceso programado CERRADO AUTOMATICO... : " + new Date());
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalTime localTime = now.toLocalTime();
      LocalDate localDate = now.toLocalDate();
      List<DateGuardUnityAssignment> activeShifts = dateGuardUnityAssignmentRepository.findLastActiveShifts(
          now);
      List<Long> activeShiftIds = activeShifts.stream().map(DateGuardUnityAssignment::getId)
          .toList();
      List<GuardAssistanceEvent> totalEvents = guardAssistanceEventRepository.findByDateGuardUnityAssignmentIdIn(
          activeShiftIds);
      for (DateGuardUnityAssignment shift : activeShifts) {
        LocalDateTime entry = shift.getDateTimeEntry();
        LocalDateTime end = shift.getDateTimeEnd();
        LocalDateTime maxMarkEndTime = end.plusMinutes(SEConstants.EXIT_AVAILABLE_TIME);
        LocalDateTime maxMarkEntryTime = entry.plusMinutes(
            SEConstants.ENTRY_TOLERANCE);

        Guard guard = shift.getGuardUnityScheduleAssignment().getGuardTypeAssignment().getGuard();
        ExternalGuard externalGuard = shift.getGuardUnityScheduleAssignment()
            .getGuardTypeAssignment().getExternalGuard();

        List<GuardAssistanceEvent> events = totalEvents.stream()
            .filter(e -> e.getDateGuardUnityAssignmentId().equals(shift.getId())).toList();
        if (events.stream().anyMatch(e -> e.getAssistanceType().equals(AssistanceType.EXIT))) {
          continue;
        }

        if (events.isEmpty()) {
          if (maxMarkEndTime.isBefore(now)) {
            //mark absence assistance event
            dateGuardUnityAssignmentRepository.updateAbsentScheduleAssistanceById(ScheduleAssignmentType.ABSENT, shift.getId());
          }
        } else {
          //mark missing events
          GuardAssistanceEvent startBreak = events.stream()
              .filter(e -> e.getAssistanceType().equals(AssistanceType.BREAK_START)).findFirst()
              .orElse(null);
          GuardAssistanceEvent endBreak = events.stream()
              .filter(e -> e.getAssistanceType().equals(AssistanceType.BREAK_END)).findFirst()
              .orElse(null);

          if (startBreak != null && endBreak == null) {
            LocalDateTime startBreakDateTime = LocalDateTime.of(startBreak.getMarkDate(),
                startBreak.getMarkTime());
            if (startBreakDateTime.plusMinutes(
                SEConstants.BREAK_TIME + SEConstants.BREAK_EXIT_TOLERANCE).isBefore(now)) {
              //mark system break end assistance event
              try {
                assistanceService.markAssist(
                    new CreateAssistanceEventRequest(
                        shift.getId(),
                        AssistanceType.BREAK_END,
                        null,
                        null,
                        null
                    ), guard, externalGuard, localDate, localTime, true, null
                );
              } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
              }
            }
          }

          if (startBreak == null && endBreak == null) {
            if (now.isAfter(end.minusMinutes(SEConstants.BREAK_AUTO_CLOSE_TIME_BEFORE_EXIT))) {
              //mark system break start and end assistance events
            }
          }

          if (now.isAfter(maxMarkEndTime)) {
            //mark system exit assistance event
            try {
              assistanceService.markAssist(
                  new CreateAssistanceEventRequest(
                      shift.getId(),
                      AssistanceType.EXIT,
                      null,
                      null,
                      null
                  ), guard, externalGuard, localDate, localTime, true, null
              );
            } catch (Exception e) {
              e.printStackTrace();
              logger.error(e.getMessage());
            }

          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Error en proceso programado", e);
    }
    logger.info("Proceso programado finalizado CERRADO AUTOMATICO... : " + new Date());
  }
}
