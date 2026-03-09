package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class SpecialServiceUnitySchedule {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long specialServiceUnityId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private SpecialServiceUnity specialServiceUnity;
  private Integer totalDays;
  private Integer totalAssignments;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  @CreationTimestamp
  private LocalDateTime createdAt;

  public SpecialServiceUnitySchedule() {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public Integer getTotalAssignments() {
    return totalAssignments;
  }

  public void setTotalAssignments(Integer totalAssignments) {
    this.totalAssignments = totalAssignments;
  }

  public Integer getTotalDays() {
    return totalDays;
  }

  public void setTotalDays(Integer totalDays) {
    this.totalDays = totalDays;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
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

}
