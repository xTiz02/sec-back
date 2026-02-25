package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.ZoneType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class EmployeeAssignmentMonthly {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long scheduleMonthlyId;
  @OneToOne
  @JoinColumn(name = "scheduleMonthlyId", referencedColumnName = "id", insertable = false,
      updatable = false)
  private ScheduleMonthly scheduleMonthly;
  private Long employeeId;
  @OneToOne
  @JoinColumn(name = "employeeId", referencedColumnName = "id", insertable = false,
      updatable = false)
  private Employee employee;
  private ZoneType zoneType;
  @CreationTimestamp
  private LocalDateTime createdAt;

  public EmployeeAssignmentMonthly() {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ScheduleMonthly getScheduleMonthly() {
    return scheduleMonthly;
  }

  public void setScheduleMonthly(ScheduleMonthly scheduleMonthly) {
    this.scheduleMonthly = scheduleMonthly;
  }

  public Long getScheduleMonthlyId() {
    return scheduleMonthlyId;
  }

  public void setScheduleMonthlyId(Long scheduleMonthlyId) {
    this.scheduleMonthlyId = scheduleMonthlyId;
  }

  public ZoneType getZoneType() {
    return zoneType;
  }

  public void setZoneType(ZoneType zoneType) {
    this.zoneType = zoneType;
  }
}
