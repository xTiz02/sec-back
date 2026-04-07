package com.prd.seccontrol.model.dto;

public interface ShiftProjection {
  Long getId();
  String getDate();

  String getGuardName();
  String getGuardCode();
  String getDocumentNumber();
  Integer getGuardType();
  Integer  getIsExternalGuard();

  String getContractUnityName();
  String getSpecialServiceUnityName();
  String getClientName();

  String getScheduledEntry();
  String getScheduledExit();

  Integer getScheduleAssignmentType();

  Boolean getHasExceptions();
  Boolean getHasExtraHours();
  Boolean getHasMarks();
  Boolean getHasVacation();
  Boolean getFinalized();
}
