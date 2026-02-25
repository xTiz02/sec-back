package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DayGuardUnitAssignmentVacation {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long dateGuardUnitAssignmentId;
  @OneToOne
  @JoinColumn(name = "dayGuardUnitAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment dayGuardUnitAssignment;
  private Long guardVacationRequestId;
  @OneToOne
  @JoinColumn(name = "guardVacationRequestId", referencedColumnName = "id",insertable = false,
      updatable = false)
  private GuardVacationRequest guardVacationRequest;

  public DayGuardUnitAssignmentVacation() {
  }

  public Long getDateGuardUnitAssignmentId() {
    return dateGuardUnitAssignmentId;
  }

  public void setDateGuardUnitAssignmentId(Long dateGuardUnitAssignmentId) {
    this.dateGuardUnitAssignmentId = dateGuardUnitAssignmentId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGuardVacationRequestId() {
    return guardVacationRequestId;
  }

  public void setGuardVacationRequestId(Long guardVacationRequestId) {
    this.guardVacationRequestId = guardVacationRequestId;
  }

  public GuardVacationRequest getGuardVacationRequest() {
    return guardVacationRequest;
  }

  public void setGuardVacationRequest(
      GuardVacationRequest guardVacationRequest) {
    this.guardVacationRequest = guardVacationRequest;
  }

  public DateGuardUnityAssignment getDayGuardUnitAssignment() {
    return dayGuardUnitAssignment;
  }

  public void setDayGuardUnitAssignment(
      DateGuardUnityAssignment dayGuardUnitAssignment) {
    this.dayGuardUnitAssignment = dayGuardUnitAssignment;
  }
}
