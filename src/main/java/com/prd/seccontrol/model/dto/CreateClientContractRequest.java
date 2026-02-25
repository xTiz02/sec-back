package com.prd.seccontrol.model.dto;

public record CreateClientContractRequest(
    String description,
    String name,
    Boolean active,
    Long clientId
) {

}
