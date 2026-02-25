package com.prd.seccontrol.model.dto;

public record LoginRequest(
    String username,
    String password
) {

}
