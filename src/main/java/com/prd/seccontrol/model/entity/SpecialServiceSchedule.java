package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.DayOfWeek;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class SpecialServiceSchedule {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long specialServiceUnityId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private SpecialServiceUnity specialServiceUnity;
  private DayOfWeek dayOfWeek;
  private Integer numDay;
  private Integer numGuards;
  private Integer numDays;
  private Integer numNights;
  private Long dayOfWeekId;
  @OneToOne
  @JoinColumn(name = "dayOfWeekId", referencedColumnName = "id", updatable = false, insertable = false)
  private WeekOfMonth weekOfMonth;

  public SpecialServiceSchedule() {
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public WeekOfMonth getWeekOfMonth() {
    return weekOfMonth;
  }

  public void setWeekOfMonth(WeekOfMonth weekOfMonth) {
    this.weekOfMonth = weekOfMonth;
  }

  public Long getSpecialServiceUnityId() {
    return specialServiceUnityId;
  }

  public void setSpecialServiceUnityId(Long specialServiceUnityId) {
    this.specialServiceUnityId = specialServiceUnityId;
  }

  public SpecialServiceUnity getSpecialServiceUnity() {
    return specialServiceUnity;
  }

  public void setSpecialServiceUnity(SpecialServiceUnity specialServiceUnity) {
    this.specialServiceUnity = specialServiceUnity;
  }

  public Integer getNumNights() {
    return numNights;
  }

  public void setNumNights(Integer numNights) {
    this.numNights = numNights;
  }

  public Integer getNumGuards() {
    return numGuards;
  }

  public void setNumGuards(Integer numGuards) {
    this.numGuards = numGuards;
  }

  public Integer getNumDays() {
    return numDays;
  }

  public void setNumDays(Integer numDays) {
    this.numDays = numDays;
  }

  public Integer getNumDay() {
    return numDay;
  }

  public void setNumDay(Integer numDay) {
    this.numDay = numDay;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDayOfWeekId() {
    return dayOfWeekId;
  }

  public void setDayOfWeekId(Long dayOfWeekId) {
    this.dayOfWeekId = dayOfWeekId;
  }
}
