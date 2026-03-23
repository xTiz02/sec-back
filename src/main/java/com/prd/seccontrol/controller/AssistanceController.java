package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateAssistanceEventRequest;
import com.prd.seccontrol.model.dto.GuardAssistanceEventDto;
import com.prd.seccontrol.model.dto.GuardCurrentShiftDto;
import com.prd.seccontrol.service.impl.AssistanceService;
import com.prd.seccontrol.util.SEConstants;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistanceController {

  @Autowired
  private AssistanceService assistanceService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-assistance/current-shift")
  public GuardCurrentShiftDto getGuardCurrentShift(Principal principal) {
    return assistanceService.getGuardCurrentShift(principal);
  }
  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-assistance/mark")
  public GuardAssistanceEventDto markAssist(@RequestBody CreateAssistanceEventRequest request,Principal principal) {
    return assistanceService.markAssist(request,principal);
  }
}
