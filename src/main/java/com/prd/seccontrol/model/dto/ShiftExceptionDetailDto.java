package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleExceptionType;

public class ShiftExceptionDetailDto {
  private Long id;
  private ScheduleExceptionType scheduleExceptionType;
  private String motive;
  private String description;
  private String replacementGuardName;
  private String replacementGuardCode;
  private GuardType replacementGuardType;
  private String replacementDocumentNumber;

  public ShiftExceptionDetailDto() {
  }

  public ScheduleExceptionType getScheduleExceptionType() {
    return scheduleExceptionType;
  }

  public void setScheduleExceptionType(
      ScheduleExceptionType scheduleExceptionType) {
    this.scheduleExceptionType = scheduleExceptionType;
  }

  public GuardType getReplacementGuardType() {
    return replacementGuardType;
  }

  public void setReplacementGuardType(GuardType replacementGuardType) {
    this.replacementGuardType = replacementGuardType;
  }

  public String getReplacementGuardName() {
    return replacementGuardName;
  }

  public void setReplacementGuardName(String replacementGuardName) {
    this.replacementGuardName = replacementGuardName;
  }

  public String getReplacementGuardCode() {
    return replacementGuardCode;
  }

  public void setReplacementGuardCode(String replacementGuardCode) {
    this.replacementGuardCode = replacementGuardCode;
  }

  public String getReplacementDocumentNumber() {
    return replacementDocumentNumber;
  }

  public void setReplacementDocumentNumber(String replacementDocumentNumber) {
    this.replacementDocumentNumber = replacementDocumentNumber;
  }

  public String getMotive() {
    return motive;
  }

  public void setMotive(String motive) {
    this.motive = motive;
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
}
