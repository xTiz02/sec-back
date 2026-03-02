package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.DayOfWeek;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class BuyFreeDay {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long weekOfMonthId;
  @OneToOne
  @JoinColumn(name = "weekOfMonthId", referencedColumnName = "id", updatable = false, insertable = false)
  private WeekOfMonth weekOfMonth;
  private DayOfWeek dayOfWeek;
  private Integer dayOfMonth;
  private LocalDate date;
  private Long guardUnityScheduleAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false, insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;

  public BuyFreeDay() {
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public WeekOfMonth getWeekOfMonth() {
    return weekOfMonth;
  }

  public void setWeekOfMonth(WeekOfMonth weekOfMonth) {
    this.weekOfMonth = weekOfMonth;
  }

  public Long getWeekOfMonthId() {
    return weekOfMonthId;
  }

  public void setWeekOfMonthId(Long weekOfMonthId) {
    this.weekOfMonthId = weekOfMonthId;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(Integer dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }

  public GuardUnityScheduleAssignment getGuardUnityScheduleAssignment() {
    return guardUnityScheduleAssignment;
  }

  public void setGuardUnityScheduleAssignment(
      GuardUnityScheduleAssignment guardUnityScheduleAssignment) {
    this.guardUnityScheduleAssignment = guardUnityScheduleAssignment;
  }

  public Long getGuardUnityScheduleAssignmentId() {
    return guardUnityScheduleAssignmentId;
  }

  public void setGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId) {
    this.guardUnityScheduleAssignmentId = guardUnityScheduleAssignmentId;
  }
}
