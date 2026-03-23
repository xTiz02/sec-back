package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.RequestStatus;
import com.prd.seccontrol.model.types.RequestType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class GuardRequest {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long guardAssistanceEventId;
  @OneToOne
  @JoinColumn(name = "guardAssistanceEventId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardAssistanceEvent guardAssistanceEvent;
  private Long guardAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardAssignment guardAssignment;
  private String description;
  private RequestType requestType;
  private RequestStatus requestStatus;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public GuardRequest() {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
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

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}
