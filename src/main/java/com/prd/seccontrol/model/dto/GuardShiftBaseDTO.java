package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDateTime;

public class GuardShiftBaseDTO {
    private Long dateGuardUnityAssignmentId;
    private String guardName;
    private String guardCode;
    private String documentNumber;
    private GuardType guardType;
    private ScheduleAssignmentType scheduleAssignmentType;
    private LocalDateTime turnStartTime;
    private LocalDateTime turnEndTime;
    private boolean isException;
    private boolean hasExtraHours;
    private boolean hasMarks;


  public GuardShiftBaseDTO(Long dateGuardUnityAssignmentId, String guardName, String guardCode,
      String documentNumber, GuardType guardType, ScheduleAssignmentType scheduleAssignmentType,
      LocalDateTime turnStartTime, LocalDateTime turnEndTime, boolean isException,
      boolean hasExtraHours, boolean hasMarks) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.guardName = guardName;
    this.guardCode = guardCode;
    this.documentNumber = documentNumber;
    this.guardType = guardType;
    this.scheduleAssignmentType = scheduleAssignmentType;
    this.turnStartTime = turnStartTime;
    this.turnEndTime = turnEndTime;
    this.isException = isException;
    this.hasExtraHours = hasExtraHours;
    this.hasMarks = hasMarks;
  }

  public boolean isHasMarks() {
    return hasMarks;
  }

  public void setHasMarks(boolean hasMarks) {
    this.hasMarks = hasMarks;
  }

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public LocalDateTime getTurnEndTime() {
    return turnEndTime;
  }

  public void setTurnEndTime(LocalDateTime turnEndTime) {
    this.turnEndTime = turnEndTime;
  }

  public LocalDateTime getTurnStartTime() {
    return turnStartTime;
  }

  public void setTurnStartTime(LocalDateTime turnStartTime) {
    this.turnStartTime = turnStartTime;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }

  public boolean isException() {
    return isException;
  }

  public void setException(boolean exception) {
    isException = exception;
  }

  public boolean isHasExtraHours() {
    return hasExtraHours;
  }

  public void setHasExtraHours(boolean hasExtraHours) {
    this.hasExtraHours = hasExtraHours;
  }

  public String getGuardName() {
    return guardName;
  }

  public void setGuardName(String guardName) {
    this.guardName = guardName;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
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
