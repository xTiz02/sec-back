package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GuardShiftDetailDto {

  private Long dateGuardUnityAssignmentId;
  private String guardName;
  private String guardCode;
  private String documentNumber;
  private GuardType guardType;
  private ScheduleAssignmentType scheduleAssignmentType;
  private LocalDateTime turnStartDateTime;
  private LocalTime turnStartTime;
  private LocalDateTime turnEndDateTime;
  private LocalTime turnEndTime;
  private boolean isException;
  private boolean hasExtraHours;
  private boolean shouldBeOnPost; // Indica si el guardia debería estar en el puesto en este momento según su turno
  private boolean isAbsent;
  private boolean hasArrived;
  private boolean isFutureShift;
  private AssistanceType lastAssistanceType;
  private LocalDateTime lastMarkTime;

  public GuardShiftDetailDto() {
  }

  public GuardShiftDetailDto(Long dateGuardUnityAssignmentId, String documentNumber,
      String guardCode,
      String guardName, GuardType guardType, ScheduleAssignmentType scheduleAssignmentType,
      LocalDateTime turnStartTime, LocalDateTime turnEndTime, boolean isException,
      boolean hasExtraHours, boolean shouldBeOnPost, boolean isAbsent, boolean hasArrived,
      boolean isFutureShift, AssistanceType lastAssistanceType, LocalDateTime lastMarkTime) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.documentNumber = documentNumber;
    this.guardCode = guardCode;
    this.guardName = guardName;
    this.hasArrived = hasArrived;
    this.hasExtraHours = hasExtraHours;
    this.guardType = guardType;
    this.isAbsent = isAbsent;
    this.isFutureShift = isFutureShift;
    this.isException = isException;
    this.lastAssistanceType = lastAssistanceType;
    this.lastMarkTime = lastMarkTime;
    this.scheduleAssignmentType = scheduleAssignmentType;
    this.shouldBeOnPost = shouldBeOnPost;
    this.turnEndTime = turnEndTime.toLocalTime();
    this.turnStartTime = turnStartTime.toLocalTime();
    this.turnStartDateTime = turnStartTime;
    this.turnEndDateTime = turnEndTime;
  }

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public LocalDateTime getLastMarkTime() {
    return lastMarkTime;
  }

  public void setLastMarkTime(LocalDateTime lastMarkTime) {
    this.lastMarkTime = lastMarkTime;
  }

  public AssistanceType getLastAssistanceType() {
    return lastAssistanceType;
  }

  public void setLastAssistanceType(AssistanceType lastAssistanceType) {
    this.lastAssistanceType = lastAssistanceType;
  }

  public boolean isFutureShift() {
    return isFutureShift;
  }

  public void setFutureShift(boolean futureShift) {
    isFutureShift = futureShift;
  }

  public boolean isException() {
    return isException;
  }

  public void setException(boolean exception) {
    isException = exception;
  }

  public boolean isAbsent() {
    return isAbsent;
  }

  public void setAbsent(boolean absent) {
    isAbsent = absent;
  }

  public boolean isHasExtraHours() {
    return hasExtraHours;
  }

  public void setHasExtraHours(boolean hasExtraHours) {
    this.hasExtraHours = hasExtraHours;
  }

  public boolean isHasArrived() {
    return hasArrived;
  }

  public void setHasArrived(boolean hasArrived) {
    this.hasArrived = hasArrived;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
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

  public LocalDateTime getTurnEndDateTime() {
    return turnEndDateTime;
  }

  public void setTurnEndDateTime(LocalDateTime turnEndDateTime) {
    this.turnEndDateTime = turnEndDateTime;
  }

  public LocalTime getTurnStartTime() {
    return turnStartTime;
  }

  public void setTurnStartTime(LocalTime turnStartTime) {
    this.turnStartTime = turnStartTime;
  }

  public LocalDateTime getTurnStartDateTime() {
    return turnStartDateTime;
  }

  public void setTurnStartDateTime(LocalDateTime turnStartDateTime) {
    this.turnStartDateTime = turnStartDateTime;
  }

  public LocalTime getTurnEndTime() {
    return turnEndTime;
  }

  public void setTurnEndTime(LocalTime turnEndTime) {
    this.turnEndTime = turnEndTime;
  }

  public boolean isShouldBeOnPost() {
    return shouldBeOnPost;
  }

  public void setShouldBeOnPost(boolean shouldBeOnPost) {
    this.shouldBeOnPost = shouldBeOnPost;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }
}
