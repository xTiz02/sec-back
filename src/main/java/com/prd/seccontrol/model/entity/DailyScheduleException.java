package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DailyScheduleException {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long guardUnityScheduleAssignmentId;

  @OneToOne
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;
  private Long scheduleExceptionId;

  @OneToOne
  @JoinColumn(name = "scheduleExceptionId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private ScheduleException scheduleException;

  private Integer orderIndex = 0;

  public DailyScheduleException() {
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public ScheduleException getScheduleException() {
    return scheduleException;
  }

  public void setScheduleException(ScheduleException scheduleException) {
    this.scheduleException = scheduleException;
  }

  public Long getScheduleExceptionId() {
    return scheduleExceptionId;
  }

  public void setScheduleExceptionId(Long scheduleExceptionId) {
    this.scheduleExceptionId = scheduleExceptionId;
  }
}
