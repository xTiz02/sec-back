package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateLateJustificationRequest;
import com.prd.seccontrol.model.dto.GuardRequestDto;
import com.prd.seccontrol.service.impl.GuarRequestService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuarRequestController {

  @Autowired
  private GuarRequestService guarRequestService;

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-request/late-justification")
  public GuardRequestDto createLateJustificationRequest(@RequestBody CreateLateJustificationRequest request) {
    return guarRequestService.justificatedLateEvent(request);
  }
}

