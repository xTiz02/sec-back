package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.GuardType;
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
  private Long guardAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardAssignmentId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private GuardAssignment guardAssignment;
  private GuardType guardType;
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

  public GuardAssignment getGuardTypeAssignment() {
    return guardAssignment;
  }

  public void setGuardTypeAssignment(
      GuardAssignment guardAssignment) {
    this.guardAssignment = guardAssignment;
  }

  public Long getGuardAssignmentId() {
    return guardAssignmentId;
  }

  public void setGuardAssignmentId(Long guardAssignmentId) {
    this.guardAssignmentId = guardAssignmentId;
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

  public GuardAssignment getGuardAssignment() {
    return guardAssignment;
  }

  public void setGuardAssignment(GuardAssignment guardAssignment) {
    this.guardAssignment = guardAssignment;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
  }

  public void setScheduleMonthlyId(Long scheduleMonthlyId) {
    this.scheduleMonthlyId = scheduleMonthlyId;
  }
}
