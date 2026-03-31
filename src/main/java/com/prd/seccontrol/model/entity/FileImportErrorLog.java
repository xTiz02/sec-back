package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.model.types.TurnType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class FileImportErrorLog {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long fileImportId;
  @OneToOne
  @JoinColumn(name = "fileImportId", referencedColumnName = "id", insertable = false, updatable = false)
  private FileImport fileImport;
  private LocalDate date;
  private String guardCode;
  private String unityCode;
  private String message;
  private TurnType turnType; // DAY, NIGHT
  private ScheduleAssignmentType scheduleAssignmentType; // FREEDAY , VACATIONAL
  private Integer dayNumber;
  private Integer fileRowNumber;
  private Integer fileColumnNumber;

  public LocalDate getDate() {
    return date;
  }

  public Integer getFileColumnNumber() {
    return fileColumnNumber;
  }

  public void setFileColumnNumber(Integer fileColumnNumber) {
    this.fileColumnNumber = fileColumnNumber;
  }

  public Integer getFileRowNumber() {
    return fileRowNumber;
  }

  public void setFileRowNumber(Integer fileRowNumber) {
    this.fileRowNumber = fileRowNumber;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Integer getDayNumber() {
    return dayNumber;
  }

  public void setDayNumber(Integer dayNumber) {
    this.dayNumber = dayNumber;
  }

  public FileImport getFileImport() {
    return fileImport;
  }

  public void setFileImport(FileImport fileImport) {
    this.fileImport = fileImport;
  }

  public Long getFileImportId() {
    return fileImportId;
  }

  public void setFileImportId(Long fileImportId) {
    this.fileImportId = fileImportId;
  }

  public String getGuardCode() {
    return guardCode;
  }

  public void setGuardCode(String guardCode) {
    this.guardCode = guardCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public TurnType getTurnType() {
    return turnType;
  }

  public void setTurnType(TurnType turnType) {
    this.turnType = turnType;
  }

  public ScheduleAssignmentType getScheduleAssignmentType() {
    return scheduleAssignmentType;
  }

  public void setScheduleAssignmentType(
      ScheduleAssignmentType scheduleAssignmentType) {
    this.scheduleAssignmentType = scheduleAssignmentType;
  }

  public String getUnityCode() {
    return unityCode;
  }

  public void setUnityCode(String unityCode) {
    this.unityCode = unityCode;
  }
}
