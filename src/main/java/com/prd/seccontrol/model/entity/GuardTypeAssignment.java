package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.GuardType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class GuardTypeAssignment {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long guardId;
  @OneToOne
  @JoinColumn(name = "guardId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private Guard guard;
  private GuardType guardType;
  private Long employeeUnitAssignmentId;
  @OneToOne
  @JoinColumn(name = "employeeUnitAssignmentId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private EmployeeUnitAssignment employeeUnitAssignment;
  private boolean active;
  @CreationTimestamp
  private LocalDateTime createdAt;

  public GuardTypeAssignment() {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public EmployeeUnitAssignment getEmployeeUnitAssignment() {
    return employeeUnitAssignment;
  }

  public void setEmployeeUnitAssignment(
      EmployeeUnitAssignment employeeUnitAssignment) {
    this.employeeUnitAssignment = employeeUnitAssignment;
  }

  public Long getEmployeeUnitAssignmentId() {
    return employeeUnitAssignmentId;
  }

  public void setEmployeeUnitAssignmentId(Long employeeUnitAssignmentId) {
    this.employeeUnitAssignmentId = employeeUnitAssignmentId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGuardId() {
    return guardId;
  }

  public void setGuardId(Long guardId) {
    this.guardId = guardId;
  }

  public Guard getGuard() {
    return guard;
  }

  public void setGuard(Guard guard) {
    this.guard = guard;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
