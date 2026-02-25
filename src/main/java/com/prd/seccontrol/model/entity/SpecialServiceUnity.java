package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SpecialServiceUnity {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private String unityName;
  private String unityDescription;

  public String getUnityName() {
    return unityName;
  }

  public void setUnityName(String unityName) {
    this.unityName = unityName;
  }

  public String getUnityDescription() {
    return unityDescription;
  }

  public void setUnityDescription(String unityDescription) {
    this.unityDescription = unityDescription;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
