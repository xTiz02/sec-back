package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class GuardUnityScheduleAssignment {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long scheduleMonthlyId;
  @OneToOne
  @JoinColumn(name = "scheduleMonthlyId", referencedColumnName = "id", updatable = false, insertable = false)
  private ScheduleMonthly scheduleMonthly;
  private Long guardTypeAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardTypeAssignmentId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private GuardTypeAssignment guardTypeAssignment;
  private Long privateGuardId;
  @OneToOne
  @JoinColumn(name = "privateGuardId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private PrivateGuard privateGuard;
  private Long contractUnityId;
  @OneToOne
  @JoinColumn(name = "contractUnityId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private ContractUnity contractUnity;
  private Long specialServiceUnityId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private SpecialServiceUnity specialServiceUnity;

  public GuardUnityScheduleAssignment() {
  }

  public ContractUnity getContractUnity() {
    return contractUnity;
  }

  public void setContractUnity(ContractUnity contractUnity) {
    this.contractUnity = contractUnity;
  }

  public Long getContractUnityId() {
    return contractUnityId;
  }

  public void setContractUnityId(Long contractUnityId) {
    this.contractUnityId = contractUnityId;
  }

  public GuardTypeAssignment getGuardTypeAssignment() {
    return guardTypeAssignment;
  }

  public void setGuardTypeAssignment(
      GuardTypeAssignment guardTypeAssignment) {
    this.guardTypeAssignment = guardTypeAssignment;
  }

  public Long getGuardTypeAssignmentId() {
    return guardTypeAssignmentId;
  }

  public void setGuardTypeAssignmentId(Long guardTypeAssignmentId) {
    this.guardTypeAssignmentId = guardTypeAssignmentId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PrivateGuard getPrivateGuard() {
    return privateGuard;
  }

  public void setPrivateGuard(PrivateGuard privateGuard) {
    this.privateGuard = privateGuard;
  }

  public Long getPrivateGuardId() {
    return privateGuardId;
  }

  public void setPrivateGuardId(Long privateGuardId) {
    this.privateGuardId = privateGuardId;
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
}
