package com.prd.seccontrol.model.dto;

import java.time.LocalDate;

public record UnitMonitoringStatusDto(
    Long contractUnityId,
    String unityName,
    String unityCode,
    String clientName,
    String clientContractName,
    String address,
    Double latitude,
    Double longitude,
    LocalDate date,
    Integer totalShifts,
    Integer arrivedGuards,
    Integer absentGuards
) {

  public UnitMonitoringStatusDto(Long contractUnityId, String unityName, String unityCode,
      String clientName, String clientContractName, String address, Double latitude,
      Double longitude,
      LocalDate date, Integer totalShifts, Integer arrivedGuards, Integer absentGuards) {
    this.contractUnityId = contractUnityId;
    this.unityName = unityName;
    this.unityCode = unityCode;
    this.clientName = clientName;
    this.clientContractName = clientContractName;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.date = date;
    this.totalShifts = totalShifts;
    this.arrivedGuards = arrivedGuards;
    this.absentGuards = absentGuards;
  }
}
