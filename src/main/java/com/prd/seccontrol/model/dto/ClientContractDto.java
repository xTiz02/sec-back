package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ClientContract;
import java.time.LocalDateTime;

public record ClientContractDto(
    Long id,
    Long clientId,
    String clientName,
    String clientCode,
    String name,
    String description,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public ClientContractDto(ClientContract clientContract) {
    this(
        clientContract.getId(),
        clientContract.getClientId(),
        clientContract.getClient() != null ? clientContract.getClient().getName() : null,
        clientContract.getClient() != null ? clientContract.getClient().getCode() : null,
        clientContract.getName(),
        clientContract.getDescription(),
        clientContract.getActive(),
        clientContract.getCreatedAt(),
        clientContract.getUpdatedAt()
    );
  }

}
