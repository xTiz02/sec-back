package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ContractUnity;

public record ContractUnityDto(
    Long id,
    Long clientContractId,
    String clientContractName,
    Long unityId,
    String unityName,
    String unityCode,
    String clientName
) {

  public ContractUnityDto(ContractUnity contractUnity) {
    this(
        contractUnity.getId(),
        contractUnity.getClientContractId(),
        contractUnity.getClientContract() != null ? contractUnity.getClientContract().getName() : null,
        contractUnity.getUnityId(),
        contractUnity.getUnity() != null ? contractUnity.getUnity().getName() : null,
        contractUnity.getUnity() != null ? contractUnity.getUnity().getCode() : null,
        contractUnity.getClientContract() != null && contractUnity.getClientContract().getClient() != null
            ? contractUnity.getClientContract().getClient().getName()
            : null
    );
  }
}
