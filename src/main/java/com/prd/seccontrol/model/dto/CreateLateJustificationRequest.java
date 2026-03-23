package com.prd.seccontrol.model.dto;

public record CreateLateJustificationRequest(
    Long guardAssistanceEventId,
    String description
) {

}
