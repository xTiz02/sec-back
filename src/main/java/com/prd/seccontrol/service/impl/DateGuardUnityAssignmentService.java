package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateBulkFreeDayRequest;
import com.prd.seccontrol.model.dto.CreateBulkVacationRequest;
import com.prd.seccontrol.model.dto.CreateDailyAssignmentRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentDto;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.entity.WeekOfMonth;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.DayOfMonthRepository;
import com.prd.seccontrol.repository.WeekOfMonthRepository;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DateGuardUnityAssignmentService {

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private DayOfMonthRepository dayOfMonthRepository;

  @Autowired
  private WeekOfMonthRepository weekOfMonthRepository;

  @Transactional
  public DateGuardUnityAssignmentDto createDateGuardUnityAssignment(
      CreateDailyAssignmentRequest request, LocalDate dateTo) {
    LocalDate date = request.date();
    Integer year = date.getYear();
    Month monthEnum = date.getMonth();
    Integer month = monthEnum.getValue();
    Integer day = date.getDayOfMonth();

    DateGuardUnityAssignment dgua = new DateGuardUnityAssignment();
    dgua.setGuardUnityScheduleAssignmentId(request.guardUnityScheduleAssignmentId());
    dgua.setTurnAndHourId(request.turnAndHourId());
    dgua.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(date.getDayOfWeek()));
    dgua.setNumDay(day);
    dgua.setDate(date);
    dgua.setToDate(dateTo);
    dgua.setScheduleAssignmentType(request.scheduleAssignmentType());
    dgua.setHasVacation(false);
    dgua.setHasExceptions(false);

    DayOfMonth dayOfMonth = dayOfMonthRepository.findByMonthAndDayOfMonthAndYear(monthEnum, day,
            year)
        .orElseGet(() -> {
          DayOfMonth newDayOfMonth = new DayOfMonth();
          newDayOfMonth.setDayOfMonth(day);
          newDayOfMonth.setMonth(monthEnum);
          newDayOfMonth.setYear(year);
          newDayOfMonth.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(date.getDayOfWeek()));

          WeekOfMonth weekOfMonth = findOrCreateWeek(date);
          newDayOfMonth.setWeekOfMonthId(weekOfMonth.getId());
          return dayOfMonthRepository.save(newDayOfMonth);
        });

    dgua.setDayOfMonthId(dayOfMonth.getId());

    DateGuardUnityAssignment saved = dateGuardUnityAssignmentRepository.save(dgua);
    return new DateGuardUnityAssignmentDto(saved);
  }

  @Transactional
  public List<DateGuardUnityAssignmentDto> bulkCreateFreeDayAssignments(
      CreateBulkFreeDayRequest request) {
    List<DateGuardUnityAssignment> existingFreeDaysAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentIdAndDateIn(
            request.guardUnityScheduleAssignmentId(), request.dates());

    List<DateGuardUnityAssignmentDto> createdAssignments = request.dates().stream()
        .filter(date -> existingFreeDaysAssignments.stream()
            .noneMatch(existing -> existing.getDate().equals(date)))
        .map(date -> {
          CreateDailyAssignmentRequest dailyRequest = new CreateDailyAssignmentRequest(
              date,
              request.guardUnityScheduleAssignmentId(),
              null, // turnAndHourId no es necesario para días libres
              ScheduleAssignmentType.FREE_DAY
          );

          return createDateGuardUnityAssignment(dailyRequest,null);
        })
        .toList();

    return createdAssignments;
  }

  @Transactional
  public List<DateGuardUnityAssignmentDto> bulkCreateVacationAssignments(
      CreateBulkVacationRequest request) {
    List<DateGuardUnityAssignment> existingAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentIdAndDateIsBetween(
            request.guardUnityScheduleAssignmentId(), request.date(), request.toDate());

    if (!existingAssignments.isEmpty()) {
      // If there are existing assignments in the date range
      return List.of();
    }

    CreateDailyAssignmentRequest dailyRequest = new CreateDailyAssignmentRequest(
        request.date(),
        request.guardUnityScheduleAssignmentId(),
        null, // turnAndHourId no es necesario para vacaciones
        ScheduleAssignmentType.VACATIONAL);

    DateGuardUnityAssignmentDto createdAssignment = createDateGuardUnityAssignment(dailyRequest, request.toDate());

    return List.of(createdAssignment);
  }


  @Transactional
  public WeekOfMonth findOrCreateWeek(LocalDate date) {

    LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
    LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

    return weekOfMonthRepository
        .findByDateFromAndDateTo(startOfWeek, endOfWeek)
        .orElseGet(() -> {

          WeekOfMonth week = new WeekOfMonth();
          week.setInitDay(startOfWeek.getDayOfMonth());
          week.setInitMonth(startOfWeek.getMonthValue());
          week.setEndDay(endOfWeek.getDayOfMonth());
          week.setEndMonth(endOfWeek.getMonthValue());
          week.setYear(startOfWeek.getYear());
          week.setOtherYear(endOfWeek.getYear());
          week.setDateFrom(startOfWeek);
          week.setDateTo(endOfWeek);

          // opcional: índice de la semana dentro del mes
          week.setOrderIndex(startOfWeek.get(WeekFields.ISO.weekOfMonth()));

          return weekOfMonthRepository.save(week);
        });
  }
}
