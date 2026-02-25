package com.prd.seccontrol.model.dto;

public record UpdateUserRequest(
    String username,
    Boolean enabled,
    Boolean accountExpired,
    Boolean accountLocked,
    Boolean credentialsExpired

) {

}
