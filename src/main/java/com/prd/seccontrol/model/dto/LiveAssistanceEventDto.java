package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.AssistanceProblemType;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.model.types.GuardType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LiveAssistanceEventDto {

  private Long id;
  private Long dateGuardUnityAssignmentId;
  private Long guardAssignmentId;
  private String guardName;
  private String guardCode;
  private String guardDocumentNumber;
  private Long contractUnityId;
  private String unityName;
  private String clientName;
  private String photoUrl;
  private Double latitude;
  private Double longitude;
  private LocalDate markDate;             // "yyyy-MM-dd"
  private LocalTime markTime;             // "HH:mm:ss"
  private LocalDateTime systemMark;           // ISO datetime
  private AssistanceType assistanceType;
  private AssistanceProblemType assistanceProblemType;
  private Long toleranceMinutes;
  private GuardType guardType;
  private Integer numberOrder;

  public LiveAssistanceEventDto() {
  }

  public LiveAssistanceEventDto(Long id, Long dateGuardUnityAssignmentId, Long guardAssignmentId, String guardName,
      String guardCode, String guardDocumentNumber, Long contractUnityId, String unityName,
      String clientName, String photoUrl, Double latitude, Double longitude, LocalDate markDate,
      LocalTime markTime, LocalDateTime systemMark, AssistanceType assistanceType,
      AssistanceProblemType assistanceProblemType, Long toleranceMinutes, GuardType guardType,
      Integer numberOrder) {
    this.assistanceProblemType = assistanceProblemType;
    this.assistanceType = assistanceType;
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.contractUnityId = contractUnityId;
    this.clientName = clientName;
    this.guardCode = guardCode;
    this.guardAssignmentId = guardAssignmentId;
    this.guardName = guardName;
    this.guardDocumentNumber = guardDocumentNumber;
    this.guardType = guardType;
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.markDate = markDate;
    this.unityName = unityName;
    this.toleranceMinutes = toleranceMinutes;
    this.systemMark = systemMark;
    this.photoUrl = photoUrl;
    this.numberOrder = numberOrder;
    this.markTime = markTime;
  }

  public AssistanceProblemType getAssistanceProblemType() {
    return assistanceProblemType;
  }

  public void setAssistanceProblemType(
      AssistanceProblemType assistanceProblemType) {
    this.assistanceProblemType = assistanceProblemType;
  }

  public AssistanceType getAssistanceType() {
    return assistanceType;
  }

  public void setAssistanceType(AssistanceType assistanceType) {
    this.assistanceType = assistanceType;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public Long getContractUnityId() {
    return contractUnityId;
  }

  public void setContractUnityId(Long contractUnityId) {
    this.contractUnityId = contractUnityId;
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

  public String getGuardCode() {
    return guardCode;
  }

  public void setGuardCode(String guardCode) {
    this.guardCode = guardCode;
  }

  public String getGuardDocumentNumber() {
    return guardDocumentNumber;
  }

  public void setGuardDocumentNumber(String guardDocumentNumber) {
    this.guardDocumentNumber = guardDocumentNumber;
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDate getMarkDate() {
    return markDate;
  }

  public void setMarkDate(LocalDate markDate) {
    this.markDate = markDate;
  }

  public LocalTime getMarkTime() {
    return markTime;
  }

  public void setMarkTime(LocalTime markTime) {
    this.markTime = markTime;
  }

  public Integer getNumberOrder() {
    return numberOrder;
  }

  public void setNumberOrder(Integer numberOrder) {
    this.numberOrder = numberOrder;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public LocalDateTime getSystemMark() {
    return systemMark;
  }

  public void setSystemMark(LocalDateTime systemMark) {
    this.systemMark = systemMark;
  }

  public Long getToleranceMinutes() {
    return toleranceMinutes;
  }

  public void setToleranceMinutes(Long toleranceMinutes) {
    this.toleranceMinutes = toleranceMinutes;
  }

  public String getUnityName() {
    return unityName;
  }

  public void setUnityName(String unityName) {
    this.unityName = unityName;
  }
}
