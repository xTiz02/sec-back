package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.RequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class GuardVacationRequest {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Integer initDay;
  private Integer initMonth;
  private Integer initYear;
  private Integer endYear;
  private Integer endMonth;
  private Integer endDay;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private String description;
  private RequestStatus requestStatus;

  public GuardVacationRequest() {
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  public Integer getInitYear() {
    return initYear;
  }

  public void setInitYear(Integer initYear) {
    this.initYear = initYear;
  }

  public Integer getInitMonth() {
    return initMonth;
  }

  public void setInitMonth(Integer initMonth) {
    this.initMonth = initMonth;
  }

  public Integer getInitDay() {
    return initDay;
  }

  public void setInitDay(Integer initDay) {
    this.initDay = initDay;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getEndYear() {
    return endYear;
  }

  public void setEndYear(Integer endYear) {
    this.endYear = endYear;
  }

  public Integer getEndMonth() {
    return endMonth;
  }

  public void setEndMonth(Integer endMonth) {
    this.endMonth = endMonth;
  }

  public Integer getEndDay() {
    return endDay;
  }

  public void setEndDay(Integer endDay) {
    this.endDay = endDay;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }
}
