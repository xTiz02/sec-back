package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.types.Country;
import com.prd.seccontrol.model.types.Gender;
import com.prd.seccontrol.model.types.IdentificationType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExternalGuardDto(
    Long id,
    Long userId,
    String firstName,
    String lastName,
    String mobilePhone,
    String email,
    Gender gender,
    String documentNumber,
    IdentificationType identificationType,
    Country country,
    String businessName,
    LocalDate birthDate,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
  public ExternalGuardDto(ExternalGuard externalGuard) {
    this(
        externalGuard.getId(),
        externalGuard.getUserId(),
        externalGuard.getFirstName(),
        externalGuard.getLastName(),
        externalGuard.getMobilePhone(),
        externalGuard.getEmail(),
        externalGuard.getGender(),
        externalGuard.getDocumentNumber(),
        externalGuard.getIdentificationType(),
        externalGuard.getCountry(),
        externalGuard.getBusinessName(),
        externalGuard.getBirthDate(),
        externalGuard.isActive(),
        externalGuard.getCreatedAt(),
        externalGuard.getUpdatedAt()
    );
  }

}
