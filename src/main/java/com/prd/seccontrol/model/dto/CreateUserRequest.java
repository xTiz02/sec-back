package com.prd.seccontrol.model.dto;

public record CreateUserRequest(
    String username,
    String password,
    String email,
    Boolean enabled
) {

}
