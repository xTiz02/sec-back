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
  private Long contractUnityId;
  @OneToOne
  @JoinColumn(name = "contractUnityId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private ContractUnity contractUnity;
  private Long specialServiceUnityScheduleId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityScheduleId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private SpecialServiceUnitySchedule specialServiceUnitySchedule;

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

  public Long getSpecialServiceUnityScheduleId() {
    return specialServiceUnityScheduleId;
  }

  public void setSpecialServiceUnityScheduleId(Long specialServiceUnityScheduleId) {
    this.specialServiceUnityScheduleId = specialServiceUnityScheduleId;
  }

  public SpecialServiceUnitySchedule getSpecialServiceUnitySchedule() {
    return specialServiceUnitySchedule;
  }

  public void setSpecialServiceUnitySchedule(
      SpecialServiceUnitySchedule specialServiceUnitySchedule) {
    this.specialServiceUnitySchedule = specialServiceUnitySchedule;
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
