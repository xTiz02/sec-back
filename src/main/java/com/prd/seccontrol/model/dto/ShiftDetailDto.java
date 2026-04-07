package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.IdentificationType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.model.types.TurnType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftDetailDto {

  private Long id;
  private LocalDate date;
  private ScheduleAssignmentType scheduleAssignmentType;
  private Boolean hasVacation;
  private Boolean hasExceptions;
  private Boolean hasExtraHours;
  private Boolean hasMarks;
  private Boolean finalized;

  private String guardName;
  private String guardCode;
  private String documentNumber;
  private IdentificationType identificationType;
  private GuardType guardType;
  private Boolean isExternalGuard;
  private String guardPhotoUrl;

  private String turnName;
  private TurnType turnType;
  private LocalDateTime scheduledEntry;
  private LocalDateTime scheduledExit;
  private LocalDateTime maxScheduledEntry;
  private LocalDateTime maxScheduledExit;

  private Long contractUnityId;
  private String contractUnityName;
  private String contractUnityCode;
  private Long specialServiceUnityId;
  private String specialServiceUnityName;
  private String clientName;
  private String clientContractName;
  private String unityAddress;

  private Long scheduleMonthlyId;
  private String scheduleMonthlyName;

  private List<ShiftAssistanceEventDetailDto> assistanceEvents = new ArrayList<>();
  private List<ShiftExceptionDetailDto> exceptions = new ArrayList<>();
  private List<ShiftExtraHoursDetailDto> extraHours = new ArrayList<>();

  public ShiftDetailDto() {
  }

  public List<ShiftAssistanceEventDetailDto> getAssistanceEvents() {
    return assistanceEvents;
  }

  public void setAssistanceEvents(
      List<ShiftAssistanceEventDetailDto> assistanceEvents) {
    this.assistanceEvents = assistanceEvents;
  }

  public String getClientContractName() {
    return clientContractName;
  }

  public void setClientContractName(String clientContractName) {
    this.clientContractName = clientContractName;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getContractUnityCode() {
    return contractUnityCode;
  }

  public void setContractUnityCode(String contractUnityCode) {
    this.contractUnityCode = contractUnityCode;
  }

  public LocalDateTime getMaxScheduledEntry() {
    return maxScheduledEntry;
  }

  public void setMaxScheduledEntry(LocalDateTime maxScheduledEntry) {
    this.maxScheduledEntry = maxScheduledEntry;
  }

  public LocalDateTime getMaxScheduledExit() {
    return maxScheduledExit;
  }

  public void setMaxScheduledExit(LocalDateTime maxScheduledExit) {
    this.maxScheduledExit = maxScheduledExit;
  }

  public Long getContractUnityId() {
    return contractUnityId;
  }

  public void setContractUnityId(Long contractUnityId) {
    this.contractUnityId = contractUnityId;
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

  public List<ShiftExceptionDetailDto> getExceptions() {
    return exceptions;
  }

  public void setExceptions(
      List<ShiftExceptionDetailDto> exceptions) {
    this.exceptions = exceptions;
  }

  public List<ShiftExtraHoursDetailDto> getExtraHours() {
    return extraHours;
  }

  public void setExtraHours(
      List<ShiftExtraHoursDetailDto> extraHours) {
    this.extraHours = extraHours;
  }

  public Boolean getFinalized() {
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

  public Boolean getHasExceptions() {
    return hasExceptions;
  }

  public void setHasExceptions(Boolean hasExceptions) {
    this.hasExceptions = hasExceptions;
  }

  public String getGuardPhotoUrl() {
    return guardPhotoUrl;
  }

  public void setGuardPhotoUrl(String guardPhotoUrl) {
    this.guardPhotoUrl = guardPhotoUrl;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
  }

  public Boolean getHasExtraHours() {
    return hasExtraHours;
  }

  public void setHasExtraHours(Boolean hasExtraHours) {
    this.hasExtraHours = hasExtraHours;
  }

  public Boolean getHasMarks() {
    return hasMarks;
  }

  public void setHasMarks(Boolean hasMarks) {
    this.hasMarks = hasMarks;
  }

  public Boolean getHasVacation() {
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

  public IdentificationType getIdentificationType() {
    return identificationType;
  }

  public void setIdentificationType(IdentificationType identificationType) {
    this.identificationType = identificationType;
  }

  public Boolean getExternalGuard() {
    return isExternalGuard;
  }

  public void setExternalGuard(Boolean externalGuard) {
    isExternalGuard = externalGuard;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }

  public LocalDateTime getScheduledEntry() {
    return scheduledEntry;
  }

  public void setScheduledEntry(LocalDateTime scheduledEntry) {
    this.scheduledEntry = scheduledEntry;
  }

  public LocalDateTime getScheduledExit() {
    return scheduledExit;
  }

  public void setScheduledExit(LocalDateTime scheduledExit) {
    this.scheduledExit = scheduledExit;
  }

  public String getScheduleMonthlyName() {
    return scheduleMonthlyName;
  }

  public void setScheduleMonthlyName(String scheduleMonthlyName) {
    this.scheduleMonthlyName = scheduleMonthlyName;
  }

  public Long getScheduleMonthlyId() {
    return scheduleMonthlyId;
  }

  public void setScheduleMonthlyId(Long scheduleMonthlyId) {
    this.scheduleMonthlyId = scheduleMonthlyId;
  }

  public Long getSpecialServiceUnityId() {
    return specialServiceUnityId;
  }

  public void setSpecialServiceUnityId(Long specialServiceUnityId) {
    this.specialServiceUnityId = specialServiceUnityId;
  }

  public String getSpecialServiceUnityName() {
    return specialServiceUnityName;
  }

  public void setSpecialServiceUnityName(String specialServiceUnityName) {
    this.specialServiceUnityName = specialServiceUnityName;
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

  public String getUnityAddress() {
    return unityAddress;
  }

  public void setUnityAddress(String unityAddress) {
    this.unityAddress = unityAddress;
  }
}
