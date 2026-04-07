package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.IdentificationType;
import java.util.Date;

public class EventAuditDto {

  private Long eventId;
  private String method; //solo son post put o delete
  private String uri;
  private Integer responseStatus;
  private Long duration;
  private Date startTime;

  // Employee
  private String employeeName;
  private String documentNumber;
  private IdentificationType identificationType;

  private String description;
  private String json; // solo si aplica puede ser null

  public EventAuditDto(Long eventId, String method, String uri, Integer responseStatus,
      Long duration, Date startTime,
      String employeeName, String documentNumber, IdentificationType identificationType,
      String description, String json) {

    this.eventId = eventId;
    this.method = method;
    this.uri = uri;
    this.responseStatus = responseStatus;
    this.duration = duration;
    this.startTime = startTime;
    this.employeeName = employeeName;
    this.documentNumber = documentNumber;
    this.identificationType = identificationType;
    this.description = description;
    this.json = json;
  }

  public EventAuditDto(EventAuditDto e) {
    this.eventId = e.eventId;
    this.method = e.method;
    this.uri = e.uri;
    this.responseStatus = e.responseStatus;
    this.duration = e.duration;
    this.startTime = e.startTime;
    this.employeeName = e.employeeName;
    this.documentNumber = e.documentNumber;
    this.identificationType = e.identificationType;
    this.description = e.description;
    this.json = e.json;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Integer getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(Integer responseStatus) {
    this.responseStatus = responseStatus;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public IdentificationType getIdentificationType() {
    return identificationType;
  }

  public void setIdentificationType(IdentificationType identificationType) {
    this.identificationType = identificationType;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }
}
