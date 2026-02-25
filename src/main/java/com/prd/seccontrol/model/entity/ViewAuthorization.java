package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ViewAuthorization {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long securityProfileId;

  private Long viewId;

  @OneToOne
  @JoinColumn(name = "viewId", referencedColumnName = "id", updatable = false, insertable = false)
  private View view;

  public ViewAuthorization() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getViewId() {
    return viewId;
  }

  public void setViewId(Long viewId) {
    this.viewId = viewId;
  }

  public View getView() {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }

  public Long getSecurityProfileId() {
    return securityProfileId;
  }

  public void setSecurityProfileId(Long securityProfileId) {
    this.securityProfileId = securityProfileId;
  }
}
