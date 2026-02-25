package com.prd.seccontrol.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class SpecialServiceTurnTime {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long specialServiceScheduleId;
  private Long turnTemplateId;
  @OneToOne
  @JoinColumn(name = "specialServiceScheduleId", referencedColumnName = "id", updatable = false, insertable = false)
  private SpecialServiceSchedule specialServiceSchedule;
  @OneToOne
  @JoinColumn(name = "turnTemplateId", referencedColumnName = "id", updatable = false, insertable = false)
  private TurnTemplate turnTemplate;

  public SpecialServiceTurnTime() {
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

  public Long getSpecialServiceScheduleId() {
    return specialServiceScheduleId;
  }

  public void setSpecialServiceScheduleId(Long specialServiceScheduleId) {
    this.specialServiceScheduleId = specialServiceScheduleId;
  }

  public SpecialServiceSchedule getSpecialServiceSchedule() {
    return specialServiceSchedule;
  }

  public void setSpecialServiceSchedule(
      SpecialServiceSchedule specialServiceSchedule) {
    this.specialServiceSchedule = specialServiceSchedule;
  }
}
