package com.prd.seccontrol.model.dto;

public record ContractUnityInfo(
    Long id,
    String unityName,
    String address,
    Double latitude,
    Double longitude,
    Double allowedRadius
) {

}
