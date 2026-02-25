package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
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
  @OneToOne
  @JoinColumn(name = "contractScheduleUnitTemplateId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private ContractScheduleUnitTemplate contractScheduleUnitTemplate;
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

  public void setTurnTemplateId(Long turnTemplateId) {
    this.turnTemplateId = turnTemplateId;
  }
}
