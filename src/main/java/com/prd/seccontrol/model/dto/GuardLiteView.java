package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.IdentificationType;

public record GuardLiteView(
    Long guardId,
    Long externalGuardId,
    String guardCode,
    String documentNumber,
    IdentificationType identificationType,
    String fullName,
    GuardType guardType
) {

  public static GuardLiteView toDto(GuardLiteProjection p) {
    return new GuardLiteView(
        p.getGuardId(),
        p.getExternalGuardId(),
        p.getGuardCode(),
        p.getDocumentNumber(),
        p.getIdentificationType() != null
            ? IdentificationType.fromOrdinal(p.getIdentificationType())
            : null,
        p.getGuardName(),
        p.getGuardType() != null
            ? GuardType.fromOrdinal(p.getGuardType())
            : null
    );
  }

}
