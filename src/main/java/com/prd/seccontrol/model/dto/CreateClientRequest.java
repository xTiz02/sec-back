package com.prd.seccontrol.model.dto;

public record CreateClientRequest(
    String name,
    String code,
    String direction,
    String description,
    Boolean active
) {

}
