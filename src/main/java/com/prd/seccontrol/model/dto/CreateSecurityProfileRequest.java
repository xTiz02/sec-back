package com.prd.seccontrol.model.dto;

public record CreateSecurityProfileRequest(
    String name,
    String description
) {

}
