package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.Country;
import com.prd.seccontrol.model.types.Gender;
import com.prd.seccontrol.model.types.IdentificationType;
import java.time.LocalDate;

public record CreateExternalGuardRequest(
  String firstName,
  String lastName,
  String email,
  String mobilePhone,
  Gender gender,
  String documentNumber,
  IdentificationType identificationType,
  Country country,
  String businessName,
  LocalDate birthDate,
  Boolean active
) {

}
