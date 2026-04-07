package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.RequestStatus;
import java.time.LocalDateTime;

public class JustificationDto {
  private Long id;
  private String description;
  private RequestStatus requestStatus;
  private LocalDateTime createdAt;

  public JustificationDto() {
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
