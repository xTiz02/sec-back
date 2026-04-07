package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.AssistanceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ShiftAssistanceEventDetailDto {
  private Long id;
  private AssistanceType assistanceType;
  private LocalDate markDate;
  private LocalTime markTime;
  private LocalDateTime systemMark;
  private LocalDateTime limitTimeToMark;
  private Long toleranceMinutes;
  private Long differenceInMinutes;
  private Integer numberOrder;
  private String photoUrl;
  private Double latitude;
  private Double longitude;
  private String ipAddress;
  private JustificationDto justification;

  public ShiftAssistanceEventDetailDto() {
  }

  public AssistanceType getAssistanceType() {
    return assistanceType;
  }

  public void setAssistanceType(AssistanceType assistanceType) {
    this.assistanceType = assistanceType;
  }

  public Long getDifferenceInMinutes() {
    return differenceInMinutes;
  }

  public void setDifferenceInMinutes(Long differenceInMinutes) {
    this.differenceInMinutes = differenceInMinutes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public JustificationDto getJustification() {
    return justification;
  }

  public void setJustification(JustificationDto justification) {
    this.justification = justification;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public LocalDateTime getLimitTimeToMark() {
    return limitTimeToMark;
  }

  public void setLimitTimeToMark(LocalDateTime limitTimeToMark) {
    this.limitTimeToMark = limitTimeToMark;
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
}
