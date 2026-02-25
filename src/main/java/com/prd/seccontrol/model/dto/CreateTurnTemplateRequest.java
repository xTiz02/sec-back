package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.TurnType;

public record CreateTurnTemplateRequest(
    String name,
    Integer numGuards,
    String timeFrom,
    String timeTo,
    TurnType turnType
) {

}

