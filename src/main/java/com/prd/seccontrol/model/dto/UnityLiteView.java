package com.prd.seccontrol.model.dto;

public record UnityLiteView(
    Long unityId,
    Long specialServiceUnityId,
    String unityName,
    String unityCode,
    String address,
    String clientName
) {
  public static UnityLiteView toDto(UnityLiteProjection p) {
    return new UnityLiteView(
        p.getUnityId(),
        p.getSpecialServiceUnityId(),
        p.getUnityName(),
        p.getUnityCode(),
        p.getAddress(),
        p.getClientName()
    );
  }
}
