package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.AssistanceProblemType;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.model.types.GuardType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    indexes = {
        @Index(name = "idx_dgua_id", columnList = "dateGuardUnityAssignmentId")
    }
)
public class GuardAssistanceEvent {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long dateGuardUnityAssignmentId;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dateGuardUnityAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private DateGuardUnityAssignment dateGuardUnityAssignment;
  private Long guardAssignmentId;
  @OneToOne
  @JoinColumn(name = "guardAssignmentId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private GuardAssignment guardAssignment;
  private String photoUrl;
  private Double latitude;
  private Double longitude;
  private LocalDate markDate;
  private LocalTime markTime;
  private LocalDateTime systemMark;
  private Integer differenceInMinutes;
  private Integer numberOrder = 0;
  private GuardType guardType;
  private String ipAddress;
  private AssistanceType assistanceType;
  private AssistanceProblemType assistanceProblemType;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public AssistanceProblemType getAssistanceProblemType() {
    return assistanceProblemType;
  }

  public GuardAssignment getGuardAssignment() {
    return guardAssignment;
  }

  public void setGuardAssignment(GuardAssignment guardAssignment) {
    this.guardAssignment = guardAssignment;
  }

  public Long getGuardAssignmentId() {
    return guardAssignmentId;
  }

  public void setGuardAssignmentId(Long guardAssignmentId) {
    this.guardAssignmentId = guardAssignmentId;
  }

  public void setAssistanceProblemType(
      AssistanceProblemType assistanceProblemType) {
    this.assistanceProblemType = assistanceProblemType;
  }

  public GuardType getGuardType() {
    return guardType;
  }

  public void setGuardType(GuardType guardType) {
    this.guardType = guardType;
  }

  public AssistanceType getAssistanceType() {
    return assistanceType;
  }

  public void setAssistanceType(AssistanceType assistanceType) {
    this.assistanceType = assistanceType;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Long getDateGuardUnityAssignmentId() {
    return dateGuardUnityAssignmentId;
  }

  public void setDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId) {
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
  }

  public DateGuardUnityAssignment getDateGuardUnityAssignment() {
    return dateGuardUnityAssignment;
  }

  public void setDateGuardUnityAssignment(
      DateGuardUnityAssignment dateGuardUnityAssignment) {
    this.dateGuardUnityAssignment = dateGuardUnityAssignment;
  }

  public Integer getDifferenceInMinutes() {
    return differenceInMinutes;
  }

  public void setDifferenceInMinutes(Integer differenceInMinutes) {
    this.differenceInMinutes = differenceInMinutes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public LocalDate getMarkDate() {
    return markDate;
  }

  public void setMarkDate(LocalDate markDate) {
    this.markDate = markDate;
  }

  public LocalTime getMarkTime() {
    return markTime;
  }

  public void setMarkTime(LocalTime markTime) {
    this.markTime = markTime;
  }

  public Integer getNumberOrder() {
    return numberOrder;
  }

  public void setNumberOrder(Integer numberOrder) {
    this.numberOrder = numberOrder;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public LocalDateTime getSystemMark() {
    return systemMark;
  }

  public void setSystemMark(LocalDateTime systemMark) {
    this.systemMark = systemMark;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}
