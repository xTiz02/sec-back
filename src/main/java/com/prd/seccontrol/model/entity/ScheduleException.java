package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.ScheduleExceptionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ScheduleException {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  private Long guardUnityScheduleAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;
  private String motive;
  private String description;
  private ScheduleExceptionType scheduleExceptionType;

  public ScheduleException() {
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ScheduleExceptionType getScheduleExceptionType() {
    return scheduleExceptionType;
  }

  public void setScheduleExceptionType(
      ScheduleExceptionType scheduleExceptionType) {
    this.scheduleExceptionType = scheduleExceptionType;
  }

  public String getMotive() {
    return motive;
  }

  public void setMotive(String motive) {
    this.motive = motive;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGuardUnityScheduleAssignmentId() {
    return guardUnityScheduleAssignmentId;
  }

  public void setGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId) {
    this.guardUnityScheduleAssignmentId = guardUnityScheduleAssignmentId;
  }

  public GuardUnityScheduleAssignment getGuardUnityScheduleAssignment() {
    return guardUnityScheduleAssignment;
  }

  public void setGuardUnityScheduleAssignment(
      GuardUnityScheduleAssignment guardUnityScheduleAssignment) {
    this.guardUnityScheduleAssignment = guardUnityScheduleAssignment;
  }
}
