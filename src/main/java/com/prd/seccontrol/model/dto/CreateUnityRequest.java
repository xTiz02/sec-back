package com.prd.seccontrol.model.dto;

public record CreateUnityRequest(
    Long clientId,
    String code,
    String name,
    String description,
    String direction,
    Double latitude,
    Double longitude,
    Double rangeCoverage,
    Boolean active
) {

}
