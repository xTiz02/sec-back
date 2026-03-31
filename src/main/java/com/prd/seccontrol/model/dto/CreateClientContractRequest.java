package com.prd.seccontrol.model.dto;

public record CreateClientContractRequest(
    String description,
    String name,
    String code,
    Boolean active,
    Long clientId
) {

}
