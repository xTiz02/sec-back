package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.AssistanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class ExitGuardAssistance {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long guardAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment dateGuardUnityAssignment;
  private String photoUrl;
  private Long gps;
  private LocalDateTime entryDateTime;
  private AssistanceType assistanceType;

  public ExitGuardAssistance() {
  }

  public AssistanceType getAssistanceType() {
    return assistanceType;
  }

  public void setAssistanceType(AssistanceType assistanceType) {
    this.assistanceType = assistanceType;
  }

  public Long getGuardAssignmentId() {
    return guardAssignmentId;
  }

  public void setGuardAssignmentId(Long guardAssignmentId) {
    this.guardAssignmentId = guardAssignmentId;
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

  public Long getGps() {
    return gps;
  }

  public void setGps(Long gps) {
    this.gps = gps;
  }

  public LocalDateTime getEntryDateTime() {
    return entryDateTime;
  }

  public void setEntryDateTime(LocalDateTime entryDateTime) {
    this.entryDateTime = entryDateTime;
  }

  public DateGuardUnityAssignment getDateGuardUnityAssignment() {
    return dateGuardUnityAssignment;
  }

  public void setDateGuardUnityAssignment(
      DateGuardUnityAssignment dateGuardUnityAssignment) {
    this.dateGuardUnityAssignment = dateGuardUnityAssignment;
  }
}
