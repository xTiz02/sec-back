package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.TurnType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalTime;

@Entity
public class TurnTemplate {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Integer numGuards;
  private LocalTime timeFrom;
  private LocalTime timeTo;
  private TurnType turnType;

  public TurnTemplate() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getNumGuards() {
    return numGuards;
  }

  public void setNumGuards(Integer numGuards) {
    this.numGuards = numGuards;
  }

  public TurnType getTurnType() {
    return turnType;
  }

  public void setTurnType(TurnType turnType) {
    this.turnType = turnType;
  }

  public LocalTime getTimeTo() {
    return timeTo;
  }

  public void setTimeTo(LocalTime timeTo) {
    this.timeTo = timeTo;
  }

  public LocalTime getTimeFrom() {
    return timeFrom;
  }

  public void setTimeFrom(LocalTime timeFrom) {
    this.timeFrom = timeFrom;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
