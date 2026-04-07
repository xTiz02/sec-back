package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateAssistanceEventRequest;
import com.prd.seccontrol.model.dto.GuardAssistanceEventDto;
import com.prd.seccontrol.model.dto.GuardCurrentShiftDto;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.model.types.AssistanceType;
import com.prd.seccontrol.repository.ExternalGuardRepository;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.repository.UserRepository;
import com.prd.seccontrol.service.impl.AssistanceService;
import com.prd.seccontrol.util.SEConstants;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AssistanceController {

  @Autowired
  private AssistanceService assistanceService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private GuardRepository guardRepository;
  @Autowired
  private ExternalGuardRepository externalGuardRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-assistance/current-shift")
  public GuardCurrentShiftDto getGuardCurrentShift(Principal principal) {
    return assistanceService.getGuardCurrentShift(principal);
  }
  @PostMapping(value = SEConstants.SECURE_BASE_ENDPOINT + "/guard-assistance/mark" ,consumes = "multipart/form-data")
  public GuardAssistanceEventDto markAssist( @RequestParam Long dateGuardUnityAssignmentId,
      @RequestParam AssistanceType assistanceType,
      @RequestParam(required = false) MultipartFile photo,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,Principal principal) throws IOException {
      CreateAssistanceEventRequest request = new CreateAssistanceEventRequest(dateGuardUnityAssignmentId,
          assistanceType, photo, latitude, longitude);
    LocalDate today = LocalDate.now();
    LocalTime todayTime = LocalTime.now();
    User user = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Guard guard = guardRepository.findByEmployee_UserId(user.getId())
        .orElse(null);

    ExternalGuard externalGuard = externalGuardRepository.findByUserId(user.getId())
        .orElse(null);

    if (guard == null && externalGuard == null) {
      throw new RuntimeException("Guard not found");
    }
    return assistanceService.markAssist(request, guard, externalGuard, today, todayTime, false, principal);
  }
}
