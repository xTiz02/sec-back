package com.prd.seccontrol.model.dto;

public record CreateSpecialServiceUnityRequest(
    String code,
    String unityName,
    String unityDescription,
    String address,
    Boolean active
) {

}
