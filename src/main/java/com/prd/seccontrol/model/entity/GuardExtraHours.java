package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class GuardExtraHours {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long dateGuardUnityAssignmentId;
  private Long guardAssistanceEventId;
  @OneToOne
  @JoinColumn(name = "guardAssistanceEventId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardAssistanceEvent guardAssistanceEvent;
  private Long guardUnityScheduleAssignmentId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guardUnityScheduleAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardUnityScheduleAssignment guardUnityScheduleAssignment;

  private Long getDateGuardUnityAssignmentId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dateGuardUnityAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment dateGuardUnityAssignment;
  private Long guardAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardAssignmentId", referencedColumnName = "id", updatable = false, insertable = false)
  private GuardAssignment guardAssignment;
  private LocalDate startDate;
  private LocalTime startTime;
  private LocalDate endDate;
  private  LocalTime endTime;
  private Integer extraHours;

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public GuardAssignment getGuardAssignment() {
    return guardAssignment;
  }

  public void setGuardAssignment(GuardAssignment guardAssignment) {
    this.guardAssignment = guardAssignment;
  }

  public Long getGuardAssignmentId() {
    return guardAssignmentId;
  }

  public void setGuardAssignmentId(Long guardAssignmentId) {
    this.guardAssignmentId = guardAssignmentId;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public DateGuardUnityAssignment getDateGuardUnityAssignment() {
    return dateGuardUnityAssignment;
  }

  public void setDateGuardUnityAssignment(
      DateGuardUnityAssignment dateGuardUnityAssignment) {
    this.dateGuardUnityAssignment = dateGuardUnityAssignment;
  }

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public Long getGetDateGuardUnityAssignmentId() {
    return getDateGuardUnityAssignmentId;
  }

  public void setGetDateGuardUnityAssignmentId(Long getDateGuardUnityAssignmentId) {
    this.getDateGuardUnityAssignmentId = getDateGuardUnityAssignmentId;
  }

  public Integer getExtraHours() {
    return extraHours;
  }

  public void setExtraHours(Integer extraHours) {
    this.extraHours = extraHours;
  }

  public GuardAssistanceEvent getGuardAssistanceEvent() {
    return guardAssistanceEvent;
  }

  public void setGuardAssistanceEvent(
      GuardAssistanceEvent guardAssistanceEvent) {
    this.guardAssistanceEvent = guardAssistanceEvent;
  }

  public Long getGuardAssistanceEventId() {
    return guardAssistanceEventId;
  }

  public void setGuardAssistanceEventId(Long guardAssistanceEventId) {
    this.guardAssistanceEventId = guardAssistanceEventId;
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

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
