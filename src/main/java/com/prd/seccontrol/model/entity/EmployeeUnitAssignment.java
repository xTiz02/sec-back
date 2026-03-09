package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.ZoneType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class EmployeeUnitAssignment {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long employeeAssignmentMonthlyId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employeeAssignmentMonthlyId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private EmployeeAssignmentMonthly employeeAssignmentMonthly;
  private Long unityId;
  @OneToOne
  @JoinColumn(name = "unityId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private Unity unity;
  private Long specialServiceUnityId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private SpecialServiceUnity specialServiceUnity;
  private ZoneType zoneType;

  public EmployeeAssignmentMonthly getEmployeeAssignmentMonthly() {
    return employeeAssignmentMonthly;
  }

  public void setEmployeeAssignmentMonthly(
      EmployeeAssignmentMonthly employeeAssignmentMonthly) {
    this.employeeAssignmentMonthly = employeeAssignmentMonthly;
  }

  public Long getEmployeeAssignmentMonthlyId() {
    return employeeAssignmentMonthlyId;
  }

  public void setEmployeeAssignmentMonthlyId(Long employeeAssignmentMonthlyId) {
    this.employeeAssignmentMonthlyId = employeeAssignmentMonthlyId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ZoneType getZoneType() {
    return zoneType;
  }

  public void setZoneType(ZoneType zoneType) {
    this.zoneType = zoneType;
  }

  public Long getUnityId() {
    return unityId;
  }

  public void setUnityId(Long unityId) {
    this.unityId = unityId;
  }

  public Unity getUnity() {
    return unity;
  }

  public SpecialServiceUnity getSpecialServiceUnity() {
    return specialServiceUnity;
  }

  public void setSpecialServiceUnity(
      SpecialServiceUnity specialServiceUnity) {
    this.specialServiceUnity = specialServiceUnity;
  }

  public Long getSpecialServiceUnityId() {
    return specialServiceUnityId;
  }

  public void setSpecialServiceUnityId(Long specialServiceUnityId) {
    this.specialServiceUnityId = specialServiceUnityId;
  }

  public void setUnity(Unity unity) {
    this.unity = unity;
  }
}
