package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.Country;
import com.prd.seccontrol.model.types.Gender;
import com.prd.seccontrol.model.types.IdentificationType;
import java.time.LocalDate;

public record CreateEmployeeRequest(
    String firstName,
    String lastName,
    String mobilePhone,
    String email,
    String address,
    String documentNumber,
    Country country,
    Gender gender,
    IdentificationType identificationType,
    LocalDate birthDate,
    Long userId
) {

}
