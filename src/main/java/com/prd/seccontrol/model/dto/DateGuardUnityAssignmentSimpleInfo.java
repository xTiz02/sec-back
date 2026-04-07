package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DateGuardUnityAssignmentSimpleInfo(
    Long id,
    Long dayOfMonthId,
    Long guardUnityScheduleAssignmentId,
//    GuardUnityScheduleAssignment guardUnityScheduleAssignment,
    Long turnAndHourId,
//    TurnAndHour turnAndHour,
    DayOfWeek dayOfWeek,
    Integer numDay,
    LocalDate date,
    ScheduleAssignmentType scheduleAssignmentType,
    LocalDate toDate,
    boolean hasVacation,
    boolean hasExceptions,
    boolean finalized,
    boolean hasExtraHours,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long contractUnityId,
    Long specialServiceId,
    LocalDateTime entry,
    LocalDateTime exit
) {
 public DateGuardUnityAssignmentSimpleInfo(Long id, Long dayOfMonthId, Long guardUnityScheduleAssignmentId,
     Long turnAndHourId, DayOfWeek dayOfWeek, Integer numDay, LocalDate date,
     ScheduleAssignmentType scheduleAssignmentType, LocalDate toDate, boolean hasVacation,
     boolean hasExceptions, boolean finalized, boolean hasExtraHours, LocalDateTime createdAt,
     LocalDateTime updatedAt, Long contractUnityId, Long specialServiceId, LocalDateTime entry, LocalDateTime exit) {

   this.id = id;
   this.dayOfMonthId = dayOfMonthId;
   this.guardUnityScheduleAssignmentId = guardUnityScheduleAssignmentId;
   this.turnAndHourId = turnAndHourId;
   this.dayOfWeek = dayOfWeek;
   this.numDay = numDay;
   this.date = date;
   this.scheduleAssignmentType = scheduleAssignmentType;
   this.toDate = toDate;
   this.hasVacation = hasVacation;
   this.hasExceptions = hasExceptions;
   this.hasExtraHours = hasExtraHours;
    this.finalized = finalized;
   this.createdAt = createdAt;
   this.updatedAt = updatedAt;
   this.contractUnityId = contractUnityId;
    this.specialServiceId = specialServiceId;
    this.entry = entry;
    this.exit = exit;
 }
}
