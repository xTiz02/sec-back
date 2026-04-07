package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateExtraHoursRequest;
import com.prd.seccontrol.service.impl.GuardExtraHoursService;
import com.prd.seccontrol.util.SEConstants;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuardExtraHoursController {

  @Autowired
  private GuardExtraHoursService guardExtraHoursService;

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-extra-hours")
  public void createGuardExtraHours(@RequestBody CreateExtraHoursRequest request, Principal principal) {
    guardExtraHoursService.assignGuardToExtraHourShift(request, principal);
  }
}
