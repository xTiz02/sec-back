package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.GuardType;
import java.time.LocalDate;
import org.springframework.cglib.core.Local;

public class DateGuardUnityAssignmentInfo {

  private String guardName;
  private String guardDocumentNumber;
  private String guardPhotoUrl;
  private GuardType guardType;
  private Long dateGuardUnityAssignmentId;
  private Long guardAssignmentId;
  private Long guardUnityAssignmentId;
  private Long seGuardUnityAssignmentId;
  private LocalDate date;
  private Long contractUnityId;
  private Long specialServiceUnityId;
  private String unityName;
  private String address;
  private Double latitude;
  private Double longitude;
  private Double allowedRadius;
  private TurnTemplate turnTemplate;
  private Long turnAndHourId;
  private boolean hasException;
  private boolean hasExtraHours;
  private boolean finalized;



  public DateGuardUnityAssignmentInfo(
      String guardName,
      String guardDocumentNumber,
      String guardPhotoUrl,
      GuardType guardType,
      Long dateGuardUnityAssignmentId,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      LocalDate date,
      Long contractUnityId,
      Long specialServiceUnityId,
      String unityName,
      String address,
      Double latitude,
      Double longitude,
      Double allowedRadius,
      TurnTemplate turnTemplate,
      Long turnAndHourId,
      boolean hasException,
      boolean hasExtraHours,
      boolean finalized
  ) {
    this.guardName = guardName;
    this.guardDocumentNumber = guardDocumentNumber;
    this.guardPhotoUrl = guardPhotoUrl;
    this.guardType = guardType;
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.guardAssignmentId = guardAssignmentId;
    this.guardUnityAssignmentId = guardUnityAssignmentId;
    this.date = date;
    this.contractUnityId = contractUnityId;
    this.specialServiceUnityId = specialServiceUnityId;
    this.unityName = unityName;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.allowedRadius = allowedRadius;
    this.turnTemplate = turnTemplate;
    this.turnAndHourId = turnAndHourId;
    this.hasException = hasException;
    this.hasExtraHours = hasExtraHours;
    this.finalized = finalized;
  }

  public DateGuardUnityAssignmentInfo(
      String guardName,
      String guardDocumentNumber,
      String guardPhotoUrl,
      GuardType guardType,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      DateGuardUnityAssignmentInfo other
  ) {
    this.guardName = guardName;
    this.guardDocumentNumber = guardDocumentNumber;
    this.guardPhotoUrl = guardPhotoUrl;
    this.guardType = guardType;
    this.dateGuardUnityAssignmentId = other.dateGuardUnityAssignmentId;
    this.guardAssignmentId = guardAssignmentId;
    this.guardUnityAssignmentId = guardUnityAssignmentId;
    this.date = other.date;
    this.contractUnityId = other.contractUnityId;
    this.specialServiceUnityId = other.specialServiceUnityId;
    this.unityName = other.unityName;
    this.address = other.address;
    this.latitude = other.latitude;
    this.longitude = other.longitude;
    this.allowedRadius = other.allowedRadius;
    this.turnTemplate = other.turnTemplate;
    this.turnAndHourId = other.turnAndHourId;
    this.hasException = other.hasException;
    this.hasExtraHours = other.hasExtraHours;
    this.finalized = other.finalized;
  }

  public DateGuardUnityAssignmentInfo(
      LocalDate date,
      GuardType guardType,
      Long dateGuardUnityAssignmentId,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      Long seGuardUnityAssignmentId,
      TurnTemplate turnTemplate,
      Long turnAndHourId,
      boolean hasException,
      boolean hasExtraHours,
      boolean finalized
  ) {
    this.date = date;
    this.guardType = guardType;
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.guardAssignmentId = guardAssignmentId;
    this.guardUnityAssignmentId = guardUnityAssignmentId;
    this.seGuardUnityAssignmentId = seGuardUnityAssignmentId;
    this.turnTemplate = turnTemplate;
    this.turnAndHourId = turnAndHourId;
    this.hasException = hasException;
    this.hasExtraHours = hasExtraHours;
    this.finalized = finalized;

  }

  public DateGuardUnityAssignmentInfo(
      Long dateGuardUnityAssignmentId,
      LocalDate date,
      Long turnAndHourId,
      boolean hasException,
      boolean hasExtraHours,
      boolean finalized
  ) {

    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.date = date;
    this.turnAndHourId = turnAndHourId;
    this.hasException = hasException;
    this.hasExtraHours = hasExtraHours;
    this.finalized = finalized;
  }


  public String getGuardDocumentNumber() {
    return guardDocumentNumber;
  }

  public void setGuardDocumentNumber(String guardDocumentNumber) {
    this.guardDocumentNumber = guardDocumentNumber;
  }

  public Long getSeGuardUnityAssignmentId() {
    return seGuardUnityAssignmentId;
  }

  public void setSeGuardUnityAssignmentId(Long seGuardUnityAssignmentId) {
    this.seGuardUnityAssignmentId = seGuardUnityAssignmentId;
  }

  public String getGuardName() {
    return guardName;
  }

  public void setGuardName(String guardName) {
    this.guardName = guardName;
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

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public Long getGuardAssignmentId() {
    return guardAssignmentId;
  }

  public void setGuardAssignmentId(Long guardAssignmentId) {
    this.guardAssignmentId = guardAssignmentId;
  }

  public Long getGuardUnityAssignmentId() {
    return guardUnityAssignmentId;
  }

  public void setGuardUnityAssignmentId(Long guardUnityAssignmentId) {
    this.guardUnityAssignmentId = guardUnityAssignmentId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getContractUnityId() {
    return contractUnityId;
  }

  public void setContractUnityId(Long contractUnityId) {
    this.contractUnityId = contractUnityId;
  }

  public Long getSpecialServiceUnityId() {
    return specialServiceUnityId;
  }

  public void setSpecialServiceUnityId(Long specialServiceUnityId) {
    this.specialServiceUnityId = specialServiceUnityId;
  }

  public String getUnityName() {
    return unityName;
  }

  public void setUnityName(String unityName) {
    this.unityName = unityName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getAllowedRadius() {
    return allowedRadius;
  }

  public void setAllowedRadius(Double allowedRadius) {
    this.allowedRadius = allowedRadius;
  }

  public TurnTemplate getTurnTemplate() {
    return turnTemplate;
  }

  public void setTurnTemplate(TurnTemplate turnTemplate) {
    this.turnTemplate = turnTemplate;
  }

  public Long getTurnAndHourId() {
    return turnAndHourId;
  }

  public void setTurnAndHourId(Long turnAndHourId) {
    this.turnAndHourId = turnAndHourId;
  }

  public boolean isHasException() {
    return hasException;
  }

  public void setHasException(boolean hasException) {
    this.hasException = hasException;
  }

  public boolean isHasExtraHours() {
    return hasExtraHours;
  }

  public void setHasExtraHours(boolean hasExtraHours) {
    this.hasExtraHours = hasExtraHours;
  }

  public boolean isFinalized() {
    return finalized;
  }

  public void setFinalized(boolean finalized) {
    this.finalized = finalized;
  }

}
