package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftSearchResultDto {

  private Long id;
  private LocalDate date;
  private String guardName;
  private String guardCode;
  private String documentNumber;
  private GuardType guardType;
  private Boolean isExternalGuard;
  private String contractUnityName;
  private String specialServiceUnityName;
  private String clientName;
  private LocalTime scheduledEntry;
  private LocalTime scheduledExit;
  private ScheduleAssignmentType scheduleAssignmentType;
  private Boolean hasExceptions;
  private Boolean hasExtraHours;
  private Boolean hasMarks;
  private Boolean hasVacation;
  private Boolean finalized;
  private LocalTime markEntry;
  private LocalTime markBreakStart;
  private LocalTime markBreakEnd;
  private LocalTime markExit;

  public ShiftSearchResultDto(ShiftProjection projection) {
    this.clientName = projection.getClientName();
    this.contractUnityName = projection.getContractUnityName();
    this.date = LocalDate.parse(projection.getDate());
    this.documentNumber = projection.getDocumentNumber();
    this.finalized = projection.getFinalized();
    this.guardCode = projection.getGuardCode();
    this.guardName = projection.getGuardName();
    this.guardType = projection.getGuardType() != null
        ? GuardType.fromOrdinal(projection.getGuardType())
        : null;
    this.hasExceptions = projection.getHasExceptions();
    this.hasExtraHours = projection.getHasExtraHours();
    this.hasMarks = projection.getHasMarks();
    this.hasVacation = projection.getHasVacation();
    this.id = projection.getId();
    this.isExternalGuard = projection.getIsExternalGuard() != null
        ? projection.getIsExternalGuard() == 1
        : null;
    this.scheduledEntry = projection.getScheduledEntry() != null
        ? LocalTime.parse(projection.getScheduledEntry())
        : null;
    this.scheduledExit = projection.getScheduledExit() != null
        ? LocalTime.parse(projection.getScheduledExit())
        : null;
    this.scheduleAssignmentType = projection.getScheduleAssignmentType() != null
        ? ScheduleAssignmentType.fromOrdinal(projection.getScheduleAssignmentType())
        : null;
    this.specialServiceUnityName = projection.getSpecialServiceUnityName();


  }

  public ShiftSearchResultDto() {
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getContractUnityName() {
    return contractUnityName;
  }

  public void setContractUnityName(String contractUnityName) {
    this.contractUnityName = contractUnityName;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public Boolean isFinalized() {
    return finalized;
  }

  public void setFinalized(Boolean finalized) {
    this.finalized = finalized;
  }

  public String getGuardCode() {
    return guardCode;
  }

  public void setGuardCode(String guardCode) {
    this.guardCode = guardCode;
  }

  public String getGuardName() {
    return guardName;
  }

  public void setGuardName(String guardName) {
    this.guardName = guardName;
  }

  public Boolean isHasExceptions() {
    return hasExceptions;
  }

  public void setHasExceptions(Boolean hasExceptions) {
    this.hasExceptions = hasExceptions;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
  }

  public Boolean isHasExtraHours() {
    return hasExtraHours;
  }

  public void setHasExtraHours(Boolean hasExtraHours) {
    this.hasExtraHours = hasExtraHours;
  }

  public Boolean isHasMarks() {
    return hasMarks;
  }

  public void setHasMarks(Boolean hasMarks) {
    this.hasMarks = hasMarks;
  }

  public Boolean isHasVacation() {
    return hasVacation;
  }

  public void setHasVacation(Boolean hasVacation) {
    this.hasVacation = hasVacation;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isExternalGuard() {
    return isExternalGuard;
  }

  public void setExternalGuard(Boolean externalGuard) {
    isExternalGuard = externalGuard;
  }

  public LocalTime getMarkBreakEnd() {
    return markBreakEnd;
  }

  public void setMarkBreakEnd(LocalTime markBreakEnd) {
    this.markBreakEnd = markBreakEnd;
  }

  public LocalTime getMarkBreakStart() {
    return markBreakStart;
  }

  public void setMarkBreakStart(LocalTime markBreakStart) {
    this.markBreakStart = markBreakStart;
  }

  public LocalTime getMarkEntry() {
    return markEntry;
  }

  public void setMarkEntry(LocalTime markEntry) {
    this.markEntry = markEntry;
  }

  public LocalTime getMarkExit() {
    return markExit;
  }

  public void setMarkExit(LocalTime markExit) {
    this.markExit = markExit;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }

  public LocalTime getScheduledEntry() {
    return scheduledEntry;
  }

  public void setScheduledEntry(LocalTime scheduledEntry) {
    this.scheduledEntry = scheduledEntry;
  }

  public LocalTime getScheduledExit() {
    return scheduledExit;
  }

  public void setScheduledExit(LocalTime scheduledExit) {
    this.scheduledExit = scheduledExit;
  }

  public String getSpecialServiceUnityName() {
    return specialServiceUnityName;
  }

  public void setSpecialServiceUnityName(String specialServiceUnityName) {
    this.specialServiceUnityName = specialServiceUnityName;
  }
}
