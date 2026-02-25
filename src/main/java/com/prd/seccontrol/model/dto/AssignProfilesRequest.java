package com.prd.seccontrol.model.dto;

import java.util.List;

public record AssignProfilesRequest(
    List<Long> profileIds
) {

}
