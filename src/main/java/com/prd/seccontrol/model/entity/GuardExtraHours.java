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
  private Long principalDateGuardUnityAssignmentId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "principalDateGuardUnityAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment principalDateGuardUnityAssignment; // turno que se esta cumpliendo

  private Long coverDateGuardUnityAssignmentId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coverDateGuardUnityAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment coverDateGuardUnityAssignment; // turno que se esta cubriendo
  private LocalDate startDate;
  private LocalTime startTime;
  private LocalDate endDate;
  private  LocalTime endTime;
  private Integer extraHours;
  private Long operatorUserId;//usuario que registra las hora extra
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "operatorUserId", referencedColumnName = "id", updatable = false, insertable = false)
  private User operatorUser;

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

  public User getOperatorUser() {
    return operatorUser;
  }

  public void setOperatorUser(User operatorUser) {
    this.operatorUser = operatorUser;
  }

  public Long getOperatorUserId() {
    return operatorUserId;
  }

  public void setOperatorUserId(Long operatorUserId) {
    this.operatorUserId = operatorUserId;
  }

  public DateGuardUnityAssignment getCoverDateGuardUnityAssignment() {
    return coverDateGuardUnityAssignment;
  }

  public void setCoverDateGuardUnityAssignment(
      DateGuardUnityAssignment coverDateGuardUnityAssignment) {
    this.coverDateGuardUnityAssignment = coverDateGuardUnityAssignment;
  }

  public Long getPrincipalDateGuardUnityAssignmentId() {
    return principalDateGuardUnityAssignmentId;
  }

  public void setPrincipalDateGuardUnityAssignmentId(Long principalDateGuardUnityAssignmentId) {
    this.principalDateGuardUnityAssignmentId = principalDateGuardUnityAssignmentId;
  }

  public DateGuardUnityAssignment getPrincipalDateGuardUnityAssignment() {
    return principalDateGuardUnityAssignment;
  }

  public void setPrincipalDateGuardUnityAssignment(
      DateGuardUnityAssignment principalDateGuardUnityAssignment) {
    this.principalDateGuardUnityAssignment = principalDateGuardUnityAssignment;
  }

  public Long getCoverDateGuardUnityAssignmentId() {
    return coverDateGuardUnityAssignmentId;
  }

  public void setCoverDateGuardUnityAssignmentId(Long coverDateGuardUnityAssignmentId) {
    this.coverDateGuardUnityAssignmentId = coverDateGuardUnityAssignmentId;
  }

  public LocalTime getEndTime() {
    return endTime;
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



  public Integer getExtraHours() {
    return extraHours;
  }

  public void setExtraHours(Integer extraHours) {
    this.extraHours = extraHours;
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
