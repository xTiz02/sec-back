package com.prd.seccontrol.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class SecurityProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  @OneToMany
  @JoinColumn(name = "securityProfileId", referencedColumnName = "id", insertable = false,
      updatable = false)
  private Set<AuthorizedEndpoint> authorizedEndpointList = new HashSet<>();
  @OneToMany
  @JoinColumn(name = "securityProfileId", referencedColumnName = "id", insertable = false,
      updatable = false)
  private Set<ViewAuthorization> viewAuthorizationList = new HashSet<>();

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
  public SecurityProfile() {
  }

  public Set<AuthorizedEndpoint> getAuthorizedEndpointList() {
    return authorizedEndpointList;
  }

  public void setAuthorizedEndpointList(
      Set<AuthorizedEndpoint> authorizedEndpointList) {
    this.authorizedEndpointList = authorizedEndpointList;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Set<ViewAuthorization> getViewAuthorizationList() {
    return viewAuthorizationList;
  }

  public void setViewAuthorizationList(
      Set<ViewAuthorization> viewAuthorizationList) {
    this.viewAuthorizationList = viewAuthorizationList;
  }
}
