package com.prd.seccontrol.model.dto;

public record UpdateSecurityProfileRequest(
    String name,
    String description
) {

}
