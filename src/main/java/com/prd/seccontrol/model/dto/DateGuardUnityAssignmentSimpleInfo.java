package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    Long specialServiceId
) {
 public DateGuardUnityAssignmentSimpleInfo(Long id, Long dayOfMonthId, Long guardUnityScheduleAssignmentId,
     Long turnAndHourId, DayOfWeek dayOfWeek, Integer numDay, LocalDate date,
     ScheduleAssignmentType scheduleAssignmentType, LocalDate toDate, boolean hasVacation,
     boolean hasExceptions, boolean finalized, boolean hasExtraHours, LocalDateTime createdAt,
     LocalDateTime updatedAt, Long contractUnityId, Long specialServiceId) {

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
 }
}
