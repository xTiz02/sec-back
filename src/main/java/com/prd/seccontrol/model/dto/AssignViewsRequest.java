package com.prd.seccontrol.model.dto;

import java.util.List;

public record AssignViewsRequest(
    List<Long> viewIds
) {

}
