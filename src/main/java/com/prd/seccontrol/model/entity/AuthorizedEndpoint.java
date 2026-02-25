package com.prd.seccontrol.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class AuthorizedEndpoint {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long securityProfileId;

  private Long endpointId;

  @OneToOne
  @JoinColumn(name = "endpointId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private Endpoint endpoint;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  public AuthorizedEndpoint() {
  }

  public Long getSecurityProfileId() {
    return securityProfileId;
  }

  public void setSecurityProfileId(Long securityProfileId) {
    this.securityProfileId = securityProfileId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEndpointId() {
    return endpointId;
  }

  public void setEndpointId(Long endpointId) {
    this.endpointId = endpointId;
  }

  public Endpoint getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(Endpoint endpoint) {
    this.endpoint = endpoint;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
