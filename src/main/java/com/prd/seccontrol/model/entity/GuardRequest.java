package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.RequestStatus;
import com.prd.seccontrol.model.types.RequestType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class GuardRequest {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long dateGuardUnityAssignmentId;
  @OneToOne
  @JoinColumn(name = "dateGuardUnityAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment dateGuardUnityAssignment;
  private String photoUrl;
  private String description;
  private RequestType requestType;
  private LocalDate date;
  private RequestStatus requestStatus;

  public GuardRequest() {
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
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

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
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

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public DateGuardUnityAssignment getDateGuardUnityAssignment() {
    return dateGuardUnityAssignment;
  }

  public void setDateGuardUnityAssignment(
      DateGuardUnityAssignment dateGuardUnityAssignment) {
    this.dateGuardUnityAssignment = dateGuardUnityAssignment;
  }
}
