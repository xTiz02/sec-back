package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class GuardVacationForWeek {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long guardVacationRequestId;
  @OneToOne
  @JoinColumn(name = "guardVacationRequestId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardVacationRequest guardVacationRequest;
  private Long weekOfMonthId;
  @OneToOne
  @JoinColumn(name = "weekOfMonthId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private WeekOfMonth weekOfMonth;
  private Integer orderIndex = 0;

  public GuardVacationRequest getGuardVacationRequest() {
    return guardVacationRequest;
  }

  public void setGuardVacationRequest(
      GuardVacationRequest guardVacationRequest) {
    this.guardVacationRequest = guardVacationRequest;
  }

  public Long getWeekOfMonthId() {
    return weekOfMonthId;
  }

  public void setWeekOfMonthId(Long weekOfMonthId) {
    this.weekOfMonthId = weekOfMonthId;
  }

  public WeekOfMonth getWeekOfMonth() {
    return weekOfMonth;
  }

  public void setWeekOfMonth(WeekOfMonth weekOfMonth) {
    this.weekOfMonth = weekOfMonth;
  }

  public Integer getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGuardVacationRequestId() {
    return guardVacationRequestId;
  }

  public void setGuardVacationRequestId(Long guardVacationRequestId) {
    this.guardVacationRequestId = guardVacationRequestId;
  }
}
