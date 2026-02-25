package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.AssistanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class ExitGuardFreeHour {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long entryGuardAssistanceId;
  @OneToOne
  @JoinColumn(name = "entryGuardAssistanceId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private EntryGuardAssistance entryGuardAssignment;
  private String photoUrl;
  private Long gps;
  private LocalDateTime entryDateTime;
  private AssistanceType assistanceType;

  public ExitGuardFreeHour() {
  }

  public AssistanceType getAssistanceType() {
    return assistanceType;
  }

  public void setAssistanceType(AssistanceType assistanceType) {
    this.assistanceType = assistanceType;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public Long getGps() {
    return gps;
  }

  public void setGps(Long gps) {
    this.gps = gps;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEntryGuardAssistanceId() {
    return entryGuardAssistanceId;
  }

  public void setEntryGuardAssistanceId(Long entryGuardAssistanceId) {
    this.entryGuardAssistanceId = entryGuardAssistanceId;
  }

  public EntryGuardAssistance getEntryGuardAssignment() {
    return entryGuardAssignment;
  }

  public void setEntryGuardAssignment(
      EntryGuardAssistance entryGuardAssignment) {
    this.entryGuardAssignment = entryGuardAssignment;
  }

  public LocalDateTime getEntryDateTime() {
    return entryDateTime;
  }

  public void setEntryDateTime(LocalDateTime entryDateTime) {
    this.entryDateTime = entryDateTime;
  }
}
