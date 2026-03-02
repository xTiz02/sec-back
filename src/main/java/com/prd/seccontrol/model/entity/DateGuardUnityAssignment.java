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
  private Long dayOfMonthId;
  @OneToOne
  @JoinColumn(name = "dayOfMonthId", referencedColumnName = "id", updatable = false, insertable = false)
  private DayOfMonth dayOfMonth;
  private Long guardUnityScheduleAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false, insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;
  private Long buyFreeDayId;
  @OneToOne
  @JoinColumn(name = "buyFreeDayId", referencedColumnName = "id", updatable = false, insertable = false)
  private BuyFreeDay buyFreeDay; // cuando compran el dia libre. por defecto en null.
  private Long turnAndHourId;
  @OneToOne
  @JoinColumn(name = "turnAndHourId", referencedColumnName = "id", updatable = false, insertable = false)
  private TurnAndHour turnAndHour;
  private DayOfWeek dayOfWeek;
  private Integer numDay;
  private LocalDate date;
  private ScheduleAssignmentType scheduleAssignmentType;
  private boolean hasVacation;
  private boolean hasExceptions;

  public DateGuardUnityAssignment() {
  }

  public BuyFreeDay getBuyFreeDay() {
    return buyFreeDay;
  }

  public void setBuyFreeDay(BuyFreeDay buyFreeDay) {
    this.buyFreeDay = buyFreeDay;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public DayOfMonth getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(DayOfMonth dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public Long getDayOfMonthId() {
    return dayOfMonthId;
  }

  public void setDayOfMonthId(Long dayOfMonthId) {
    this.dayOfMonthId = dayOfMonthId;
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

  public Long getBuyFreeDayId() {
    return buyFreeDayId;
  }

  public void setBuyFreeDayId(Long buyFreeDayId) {
    this.buyFreeDayId = buyFreeDayId;
  }

  public BuyFreeDay getFreeDay() {
    return buyFreeDay;
  }

  public void setFreeDay(BuyFreeDay buyFreeDay) {
    this.buyFreeDay = buyFreeDay;
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
