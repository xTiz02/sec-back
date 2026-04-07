package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.IdentificationType;
import com.prd.seccontrol.model.types.TurnType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NextShiftToCoverDto{
   private Long id;
   private LocalDate date;
   private String guardName;
   private String guardCode;
   private String documentNumber;
   private IdentificationType identificationType;
   private LocalTime scheduledEntry;
   private LocalTime scheduledExit;
   private String turnName;
   private TurnType turnType;


  public NextShiftToCoverDto(Long id, LocalDate date, String guardName, String guardCode,
      String documentNumber, IdentificationType identificationType, LocalDateTime scheduledEntry,
      LocalDateTime scheduledExit, String turnName, TurnType turnType) {

    this.id = id;
    this.date = date;
    this.guardName = guardName;
    this.guardCode = guardCode;
    this.documentNumber = documentNumber;
    this.identificationType = identificationType;
    this.scheduledEntry = scheduledEntry.toLocalTime();
    this.scheduledExit = scheduledExit.toLocalTime();
    this.turnName = turnName;
    this.turnType = turnType;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getTurnName() {
    return turnName;
  }

  public void setTurnName(String turnName) {
    this.turnName = turnName;
  }

  public TurnType getTurnType() {
    return turnType;
  }

  public void setTurnType(TurnType turnType) {
    this.turnType = turnType;
  }

  public LocalTime getScheduledExit() {
    return scheduledExit;
  }

  public void setScheduledExit(LocalTime scheduledExit) {
    this.scheduledExit = scheduledExit;
  }

  public LocalTime getScheduledEntry() {
    return scheduledEntry;
  }

  public void setScheduledEntry(LocalTime scheduledEntry) {
    this.scheduledEntry = scheduledEntry;
  }

  public IdentificationType getIdentificationType() {
    return identificationType;
  }

  public void setIdentificationType(IdentificationType identificationType) {
    this.identificationType = identificationType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGuardName() {
    return guardName;
  }

  public void setGuardName(String guardName) {
    this.guardName = guardName;
  }

  public String getGuardCode() {
    return guardCode;
  }

  public void setGuardCode(String guardCode) {
    this.guardCode = guardCode;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }
}
