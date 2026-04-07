package com.prd.seccontrol.model.dto;

import java.time.LocalDate;

public interface UnitMonitoringStatusResume {
  Long getContractUnityId();
  String getUnityName();
  String getUnityCode();
  String getClientName();
  String getClientContractName();
  String getAddress();
  Double getLatitude();
  Double getLongitude();
  LocalDate getDate();
  Integer getTotalShifts();
  Integer getArrivedGuards();
  Integer getAbsentGuards();
}
