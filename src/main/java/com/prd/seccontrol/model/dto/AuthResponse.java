package com.prd.seccontrol.model.dto;

public record AuthResponse(
    String token
) {

  public AuthResponse(String token) {
    this.token = token;
  }
}
