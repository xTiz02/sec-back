package com.prd.seccontrol.model.dto;

import java.util.List;

public record AssignEndpointsRequest(
    List<Long> endpointIds
) {

}
