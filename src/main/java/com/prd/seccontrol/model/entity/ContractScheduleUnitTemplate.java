package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.DayOfWeek;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ContractScheduleUnitTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long contractUnityId;
  @OneToOne
  @JoinColumn(name = "contractUnityId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private ContractUnity contractUnity;
  private DayOfWeek dayOfWeek;
  private Integer numOfGuards;
  private Integer numTurnDay;
  private Integer numTurnNight;

  public ContractScheduleUnitTemplate() {
  }

  public ContractUnity getContractUnity() {
    return contractUnity;
  }

  public void setContractUnity(ContractUnity contractUnity) {
    this.contractUnity = contractUnity;
  }

  public Integer getNumTurnNight() {
    return numTurnNight;
  }

  public void setNumTurnNight(Integer numTurnNight) {
    this.numTurnNight = numTurnNight;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public Long getContractUnityId() {
    return contractUnityId;
  }

  public void setContractUnityId(Long contractUnityId) {
    this.contractUnityId = contractUnityId;
  }

  public Integer getNumTurnDay() {
    return numTurnDay;
  }

  public void setNumTurnDay(Integer numTurnDay) {
    this.numTurnDay = numTurnDay;
  }

  public Integer getNumOfGuards() {
    return numOfGuards;
  }

  public void setNumOfGuards(Integer numOfGuards) {
    this.numOfGuards = numOfGuards;
  }
}
