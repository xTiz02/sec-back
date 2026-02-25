package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.types.DayOfWeek;
import java.util.List;

public record DayTurnAssignment(
    DayOfWeek dayOfWeek,
    List<Long> turnTemplateIds
) {

}
