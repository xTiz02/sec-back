package com.prd.seccontrol.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ShiftExtraHoursDetailDto {

  private Long id;
  private LocalDate startDate;
  private LocalTime startTime;
  private LocalDate endDate;
  private LocalTime endTime;
  private Integer extraHours;
  private String principalGuardName;
  private LocalDate principalShiftDate;
  private String coverGuardName;
  private LocalDate coverShiftDate;
  private String operatorUserName;
  private LocalDateTime createdAt;

  public ShiftExtraHoursDetailDto() {
  }

  public String getCoverGuardName() {
    return coverGuardName;
  }

  public void setCoverGuardName(String coverGuardName) {
    this.coverGuardName = coverGuardName;
  }

  public LocalDate getCoverShiftDate() {
    return coverShiftDate;
  }

  public void setCoverShiftDate(LocalDate coverShiftDate) {
    this.coverShiftDate = coverShiftDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
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

  public String getOperatorUserName() {
    return operatorUserName;
  }

  public void setOperatorUserName(String operatorUserName) {
    this.operatorUserName = operatorUserName;
  }

  public String getPrincipalGuardName() {
    return principalGuardName;
  }

  public void setPrincipalGuardName(String principalGuardName) {
    this.principalGuardName = principalGuardName;
  }

  public LocalDate getPrincipalShiftDate() {
    return principalShiftDate;
  }

  public void setPrincipalShiftDate(LocalDate principalShiftDate) {
    this.principalShiftDate = principalShiftDate;
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
}
