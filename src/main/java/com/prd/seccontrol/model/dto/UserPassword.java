package com.prd.seccontrol.model.dto;

import jakarta.validation.constraints.NotNull;

public record UserPassword(
    @NotNull
    String currentPassword,
    @NotNull
    String newPassword,
    @NotNull
    String confirmPassword
) {

}
