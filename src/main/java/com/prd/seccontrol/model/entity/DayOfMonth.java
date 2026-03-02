package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.DayOfWeek;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class DayOfMonth {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Integer dayOfMonth;
  private Month month;
  private Integer year;
  private DayOfWeek dayOfWeek;
  private Long weekOfMonthId;
  @OneToOne
  @JoinColumn(name = "weekOfMonthId", referencedColumnName = "id", updatable = false, insertable = false)
  private WeekOfMonth weekOfMonth;
  private LocalDate date;
  @CreationTimestamp
  private LocalDateTime createdAt;

  public DayOfMonth() {
  }
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Month getMonth() {
    return month;
  }

  public void setMonth(Month month) {
    this.month = month;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getDayOfMonth() {
    return dayOfMonth;
  }

  public void setDayOfMonth(Integer dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
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

  public Long getWeekOfMonthId() {
    return weekOfMonthId;
  }

  public void setWeekOfMonthId(Long weekOfMonthId) {
    this.weekOfMonthId = weekOfMonthId;
  }
}
