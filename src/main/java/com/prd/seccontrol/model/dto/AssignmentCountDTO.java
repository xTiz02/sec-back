package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.ContractUnity;

public record AssignmentCountDTO(
    ContractUnity contractUnity, Long count
) {

}
