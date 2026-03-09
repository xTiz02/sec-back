package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class TurnAndHour {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long contractScheduleUnitTemplateId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contractScheduleUnitTemplateId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private ContractScheduleUnitTemplate contractScheduleUnitTemplate;
  private Long specialServiceUnityScheduleId;
  @OneToOne
  @JoinColumn(name = "specialServiceUnityScheduleId", referencedColumnName = "id", updatable = false, insertable = false)
  private SpecialServiceUnitySchedule specialServiceUnitySchedule;
  private Long turnTemplateId;
  @OneToOne
  @JoinColumn(name = "turnTemplateId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private TurnTemplate turnTemplate;

  public TurnAndHour() {
  }

  public Long getContractScheduleUnitTemplateId() {
    return contractScheduleUnitTemplateId;
  }

  public void setContractScheduleUnitTemplateId(Long contractScheduleUnitTemplateId) {
    this.contractScheduleUnitTemplateId = contractScheduleUnitTemplateId;
  }

  public ContractScheduleUnitTemplate getContractScheduleUnitTemplate() {
    return contractScheduleUnitTemplate;
  }

  public void setContractScheduleUnitTemplate(
      ContractScheduleUnitTemplate contractScheduleUnitTemplate) {
    this.contractScheduleUnitTemplate = contractScheduleUnitTemplate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TurnTemplate getTurnTemplate() {
    return turnTemplate;
  }

  public void setTurnTemplate(TurnTemplate turnTemplate) {
    this.turnTemplate = turnTemplate;
  }

  public Long getTurnTemplateId() {
    return turnTemplateId;
  }

  public SpecialServiceUnitySchedule getSpecialServiceSchedule() {
    return specialServiceUnitySchedule;
  }

  public void setSpecialServiceSchedule(
      SpecialServiceUnitySchedule specialServiceUnitySchedule) {
    this.specialServiceUnitySchedule = specialServiceUnitySchedule;
  }

  public SpecialServiceUnitySchedule getSpecialServiceUnitySchedule() {
    return specialServiceUnitySchedule;
  }

  public void setSpecialServiceUnitySchedule(
      SpecialServiceUnitySchedule specialServiceUnitySchedule) {
    this.specialServiceUnitySchedule = specialServiceUnitySchedule;
  }

  public Long getSpecialServiceUnityScheduleId() {
    return specialServiceUnityScheduleId;
  }

  public void setSpecialServiceUnityScheduleId(Long specialServiceUnityScheduleId) {
    this.specialServiceUnityScheduleId = specialServiceUnityScheduleId;
  }

  public void setTurnTemplateId(Long turnTemplateId) {
    this.turnTemplateId = turnTemplateId;
  }
}
