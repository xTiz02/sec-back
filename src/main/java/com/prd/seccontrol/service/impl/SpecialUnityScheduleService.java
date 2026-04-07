package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateSpecialServiceAssignmentRequest;
import com.prd.seccontrol.model.dto.CreateSpecialServiceScheduleRequest;
import com.prd.seccontrol.model.dto.GuardAssignmentDto;
import com.prd.seccontrol.model.dto.SpecialServiceDayAssignmentDto;
import com.prd.seccontrol.model.dto.SpecialServiceGuardUnityAssignmentDto;
import com.prd.seccontrol.model.dto.SpecialServiceScheduleDto;
import com.prd.seccontrol.model.dto.TurnAndHourDto;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.SpecialServiceUnity;
import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.entity.WeekOfMonth;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.DayOfMonthRepository;
import com.prd.seccontrol.repository.ExternalGuardRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.SpecialServiceUnityRepository;
import com.prd.seccontrol.repository.SpecialServiceUnityScheduleRepository;
import com.prd.seccontrol.repository.TurnAndHourRepository;
import com.prd.seccontrol.repository.TurnTemplateRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialUnityScheduleService {

  @Autowired
  private SpecialServiceUnityScheduleRepository specialUnityScheduleRepository;

  @Autowired
  private SpecialServiceUnityRepository specialServiceUnityRepository;

  @Autowired
  private GuardRepository guardRepository;

  @Autowired
  private ExternalGuardRepository externalGuardRepository;

  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private SpecialServiceUnityScheduleRepository specialServiceUnityScheduleRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private TurnAndHourRepository turnAndHourRepository;

  @Autowired
  private DayOfMonthRepository dayOfMonthRepository;

  @Autowired
  private DateGuardUnityAssignmentService dateGuardUnityAssignmentService;

  @Transactional
  public SpecialServiceScheduleDto createSpecialServiceUnitySchedule(
      CreateSpecialServiceScheduleRequest request) {

    SpecialServiceUnity specialServiceUnity = specialServiceUnityRepository.findById(
            request.specialServiceUnityId())
        .orElseThrow(() -> new RuntimeException(
            "Special Service Unity not found with id: " + request.specialServiceUnityId()));
    List<CreateSpecialServiceAssignmentRequest> assignments = request.assignments();

    LocalDate minDate = assignments.stream().map(CreateSpecialServiceAssignmentRequest::date)
        .min(LocalDate::compareTo)
        .orElseThrow(() -> new RuntimeException("No assignments provided"));
    LocalDate maxDate = assignments.stream().map(CreateSpecialServiceAssignmentRequest::date)
        .max(LocalDate::compareTo)
        .orElseThrow(() -> new RuntimeException("No assignments provided"));
    Integer countAssignments = assignments.size();
    long distinctDates = assignments.stream().map(CreateSpecialServiceAssignmentRequest::date)
        .distinct().count();

    SpecialServiceUnitySchedule schedule = new SpecialServiceUnitySchedule();
    schedule.setSpecialServiceUnityId(specialServiceUnity.getId());
    schedule.setDateFrom(minDate);
    schedule.setDateTo(maxDate);
    schedule.setTotalAssignments(countAssignments);
    schedule.setTotalDays((Integer.valueOf(String.valueOf(distinctDates))));
    schedule = specialUnityScheduleRepository.save(schedule);

    List<SpecialServiceDayAssignmentDto> dayAssignmentDtos = new ArrayList<>();

    for (CreateSpecialServiceAssignmentRequest assignment : assignments) {
      Guard guard = null;
      ExternalGuard externalGuard = null;
      if (assignment.guardId() != null) {
        guard = guardRepository.findById(assignment.guardId())
            .orElseThrow(
                () -> new RuntimeException("Guard not found with id: " + assignment.guardId()));
      }

      if (assignment.externalGuardId() != null) {
        externalGuard = externalGuardRepository.findById(assignment.externalGuardId())
            .orElseThrow(() -> new RuntimeException(
                "External Guard not found with id: " + assignment.externalGuardId()));
      }

      LocalDate assignmentDate = assignment.date();
      Integer year = assignmentDate.getYear();
      Month monthEnum = assignmentDate.getMonth();
      Integer month = monthEnum.getValue();
      Integer day = assignmentDate.getDayOfMonth();

      TurnTemplate turnTemplate = turnTemplateRepository.findByTimeFromAndTimeToAndTurnTypeAndNumGuards(
              assignment.timeFrom(), assignment.timeTo(), assignment.turnType(), 1)
          .orElse(null);

      if (turnTemplate == null) {
        TurnTemplate newTurnTemplate = new TurnTemplate();
        newTurnTemplate.setTimeFrom(assignment.timeFrom());
        newTurnTemplate.setTimeTo(assignment.timeTo());
        newTurnTemplate.setTurnType(assignment.turnType());
        newTurnTemplate.setNumGuards(1);
        LocalDateTime now = LocalDateTime.now();
        // set format year-month-day-hour-minute-second
        String name = String.format("%d-%02d-%02d-%02d-%02d", now.getYear(), now.getMonthValue(),
            now.getDayOfMonth(), now.getHour(), now.getMinute());
        newTurnTemplate.setName("SUS - " + name);
        turnTemplate = turnTemplateRepository.save(newTurnTemplate);
      }

      TurnAndHour turnAndHour = new TurnAndHour();
      turnAndHour.setTurnTemplateId(turnTemplate.getId());
      turnAndHour.setSpecialServiceUnityScheduleId(schedule.getId());
      turnAndHour = turnAndHourRepository.save(turnAndHour);

      GuardUnityScheduleAssignment existingAssignment = guardUnityScheduleAssignmentRepository.findByGuardIdAndSpecialServiceUnityScheduleId(
          assignment.guardId(), assignment.externalGuardId(), schedule.getId()).orElse(null);

      if (existingAssignment == null) {
        GuardAssignment guardAssignment = new GuardAssignment();
        guardAssignment.setGuardId(guard != null ? guard.getId() : null);
        guardAssignment.setExternalGuardId(externalGuard != null ? externalGuard.getId() : null);
        guardAssignment.setActive(true);
        guardAssignment = guardAssignmentRepository.save(guardAssignment);

        GuardUnityScheduleAssignment guardUnityScheduleAssignment = new GuardUnityScheduleAssignment();
        guardUnityScheduleAssignment.setSpecialServiceUnityScheduleId(schedule.getId());
        guardUnityScheduleAssignment.setGuardAssignmentId(guardAssignment.getId());
        guardUnityScheduleAssignment.setGuardType(assignment.guardType());

        existingAssignment = guardUnityScheduleAssignmentRepository.save(
            guardUnityScheduleAssignment);
      }

      DateGuardUnityAssignment dgua = new DateGuardUnityAssignment();
      dgua.setGuardUnityScheduleAssignmentId(existingAssignment.getId());
      dgua.setTurnAndHourId(turnAndHour.getId());
      dgua.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(assignmentDate.getDayOfWeek()));
      dgua.setNumDay(day);
      dgua.setDate(assignmentDate);
      dgua.setScheduleAssignmentType(ScheduleAssignmentType.NORMAL);
      dgua.setHasVacation(false);
      dgua.setHasExceptions(false);

      DayOfMonth dayOfMonth = dayOfMonthRepository.findByMonthAndDayOfMonthAndYear(monthEnum, day,
              year)
          .orElseGet(() -> {
            DayOfMonth newDayOfMonth = new DayOfMonth();
            newDayOfMonth.setDayOfMonth(day);
            newDayOfMonth.setMonth(monthEnum);
            newDayOfMonth.setYear(year);
            newDayOfMonth.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(assignmentDate.getDayOfWeek()));
            newDayOfMonth.setDate(assignmentDate);

            WeekOfMonth weekOfMonth = dateGuardUnityAssignmentService.findOrCreateWeek(
                assignmentDate);
            newDayOfMonth.setWeekOfMonthId(weekOfMonth.getId());
            return dayOfMonthRepository.save(newDayOfMonth);
          });

      dgua.setDayOfMonthId(dayOfMonth.getId());

      DateGuardUnityAssignment dguaSaved = dateGuardUnityAssignmentRepository.save(dgua);
      SpecialServiceDayAssignmentDto assignmentDto = new SpecialServiceDayAssignmentDto(
          dguaSaved.getId(), dguaSaved.getDate(), new SpecialServiceGuardUnityAssignmentDto(
          existingAssignment.getId(), existingAssignment.getGuardType(),
          new GuardAssignmentDto(
              guardAssignmentRepository.findById(existingAssignment.getId())
                  .orElseThrow(() -> new RuntimeException(
                      "Guard Assignment not found with id: ")))), new TurnAndHourDto(turnAndHour));

      dayAssignmentDtos.add(assignmentDto);
    }

    SpecialServiceScheduleDto scheduleDto = new SpecialServiceScheduleDto(schedule.getId(),
        specialServiceUnity.getId(), specialServiceUnity, schedule.getDateFrom(),
        schedule.getDateTo(), schedule.getTotalDays(), schedule.getTotalAssignments(),
        dayAssignmentDtos, schedule.getCreatedAt());

    return scheduleDto;
  }

  public SpecialServiceScheduleDto getSpecialServiceUnitySchedule(Long id) {
    SpecialServiceUnitySchedule schedule = specialUnityScheduleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException(
            "Special Service Unity Schedule not found with id: " + id));

    List<SpecialServiceDayAssignmentDto> dayAssignmentDtos = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignment_SpecialServiceUnityScheduleId(schedule.getId())
        .stream()
        .map(dgua -> {
          TurnAndHour turnAndHour = dgua.getTurnAndHour();
          SpecialServiceGuardUnityAssignmentDto  ssgua = getGuardAssignmentForSpecialServiceUnitySchedule(dgua);

          return new SpecialServiceDayAssignmentDto(dgua.getId(), dgua.getDate(),ssgua
              , new TurnAndHourDto(turnAndHour));
        })
        .toList();

    SpecialServiceScheduleDto scheduleDto = new SpecialServiceScheduleDto(schedule.getId(),
        schedule.getSpecialServiceUnity().getId(), schedule.getSpecialServiceUnity(),
        schedule.getDateFrom(), schedule.getDateTo(), schedule.getTotalDays(),
        schedule.getTotalAssignments(), dayAssignmentDtos, schedule.getCreatedAt());

    return scheduleDto;
  }

  public List<SpecialServiceGuardUnityAssignmentDto> getGuardAssignmentsForSpecialServiceUnitySchedule(Long scheduleId) {
    List<SpecialServiceGuardUnityAssignmentDto> guardUnityAssignmentDtos = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignment_SpecialServiceUnityScheduleId(scheduleId)
        .stream()
        .map(this::getGuardAssignmentForSpecialServiceUnitySchedule)
        .toList();

    return guardUnityAssignmentDtos;
  }

  public SpecialServiceGuardUnityAssignmentDto getGuardAssignmentForSpecialServiceUnitySchedule(DateGuardUnityAssignment dgua) {
    GuardUnityScheduleAssignment guardUnityScheduleAssignment = dgua.getGuardUnityScheduleAssignment();
    GuardAssignment guardAssignment = guardUnityScheduleAssignment.getGuardAssignment();

    return new SpecialServiceGuardUnityAssignmentDto(guardUnityScheduleAssignment.getId(),
        guardUnityScheduleAssignment.getGuardType(),
        new GuardAssignmentDto(guardAssignment));
  }
}
