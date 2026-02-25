package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class DateGuardUnityAssignment {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long weekOfMonthId;
  @OneToOne
  @JoinColumn(name = "weekOfMonthId", referencedColumnName = "id", updatable = false, insertable = false)
  private WeekOfMonth weekOfMonth;
  private Long guardUnityScheduleAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false, insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;
  private Long freeDayId;
  @OneToOne
  @JoinColumn(name = "freeDayId", referencedColumnName = "id", updatable = false, insertable = false)
  private FreeDay freeDay;
  private Long turnAndHourId;
  @OneToOne
  @JoinColumn(name = "turnAndHourId", referencedColumnName = "id", updatable = false, insertable = false)
  private TurnAndHour turnAndHour;
  private DayOfWeek dayOfWeek;
//  private Long unityMonthSchedule;
//  @OneToOne
//  @JoinColumn(name = "unityMonthSchedule", referencedColumnName = "id", updatable = false, insertable = false)
//  private UnityMonthSchedule unityMonthScheduleObj;
  private Integer numDay;
  private LocalDate date;
  private ScheduleAssignmentType scheduleAssignmentType;
  private boolean hasVacation;
  private boolean hasExceptions;

  public DateGuardUnityAssignment() {
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getWeekOfMonthId() {
    return weekOfMonthId;
  }

  public void setWeekOfMonthId(Long weekOfMonthId) {
    this.weekOfMonthId = weekOfMonthId;
  }

  public WeekOfMonth getWeekOfMonth() {
    return weekOfMonth;
  }

  public void setWeekOfMonth(WeekOfMonth weekOfMonth) {
    this.weekOfMonth = weekOfMonth;
  }

  public Long getTurnAndHourId() {
    return turnAndHourId;
  }

  public void setTurnAndHourId(Long turnAndHourId) {
    this.turnAndHourId = turnAndHourId;
  }

  public TurnAndHour getTurnAndHour() {
    return turnAndHour;
  }

  public void setTurnAndHour(TurnAndHour turnAndHour) {
    this.turnAndHour = turnAndHour;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }

  public Integer getNumDay() {
    return numDay;
  }

  public void setNumDay(Integer numDay) {
    this.numDay = numDay;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public GuardUnityScheduleAssignment getGuardUnityScheduleAssignment() {
    return guardUnityScheduleAssignment;
  }

  public void setGuardUnityScheduleAssignment(
      GuardUnityScheduleAssignment guardUnityScheduleAssignment) {
    this.guardUnityScheduleAssignment = guardUnityScheduleAssignment;
  }

  public Long getFreeDayId() {
    return freeDayId;
  }

  public void setFreeDayId(Long freeDayId) {
    this.freeDayId = freeDayId;
  }

  public FreeDay getFreeDay() {
    return freeDay;
  }

  public void setFreeDay(FreeDay freeDay) {
    this.freeDay = freeDay;
  }

  public boolean isHasVacation() {
    return hasVacation;
  }

  public void setHasVacation(boolean hasVacation) {
    this.hasVacation = hasVacation;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

//  public Long getUnityMonthSchedule() {
//    return unityMonthSchedule;
//  }
//
//  public void setUnityMonthSchedule(Long unityMonthSchedule) {
//    this.unityMonthSchedule = unityMonthSchedule;
//  }
//
//  public UnityMonthSchedule getUnityMonthScheduleObj() {
//    return unityMonthScheduleObj;
//  }
//
//  public void setUnityMonthScheduleObj(
//      UnityMonthSchedule unityMonthScheduleObj) {
//    this.unityMonthScheduleObj = unityMonthScheduleObj;
//  }

  public boolean isHasExceptions() {
    return hasExceptions;
  }

  public void setHasExceptions(boolean hasExceptions) {
    this.hasExceptions = hasExceptions;
  }

  public Long getGuardUnityScheduleAssignmentId() {
    return guardUnityScheduleAssignmentId;
  }

  public void setGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId) {
    this.guardUnityScheduleAssignmentId = guardUnityScheduleAssignmentId;
  }
}
