package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.AssignEndpointsRequest;
import com.prd.seccontrol.model.dto.AssignViewsRequest;
import com.prd.seccontrol.model.dto.CreateSecurityProfileRequest;
import com.prd.seccontrol.model.dto.SecurityProfileDto;
import com.prd.seccontrol.model.dto.UpdateSecurityProfileRequest;
import com.prd.seccontrol.model.entity.SecurityProfile;
import com.prd.seccontrol.repository.SecurityProfileRepository;
import com.prd.seccontrol.service.impl.SecurityProfileService;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityProfileController {
  @Autowired
  private SecurityProfileRepository securityProfileRepository;

  @Autowired
  private SecurityProfileService securityProfileService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/all")
  public List<SecurityProfileDto> findAll() {
    return securityProfileRepository.findAll().stream().map(SecurityProfileDto::new).toList();
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/{id}")
  public SecurityProfile findById(@PathVariable(value = "id") Long id) throws Exception {
    return securityProfileRepository.findById(id)
        .orElseThrow(() -> new Exception("Security Profile not found with id: " + id));
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles")
  public SecurityProfile create(@RequestBody CreateSecurityProfileRequest request) {
    return securityProfileService.create(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/{id}")
  public SecurityProfile update(@RequestBody UpdateSecurityProfileRequest request,@PathVariable(value = "id") Long id) throws Exception {
    return securityProfileService.update(id, request);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/{id}")
  public Long delete(@PathVariable(value = "id") Long id) {
    securityProfileRepository.deleteById(id);
    return id;
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/{id}/views")
  public SecurityProfile assignViewsAuthorization(@PathVariable(value = "id") Long id,
      @RequestBody AssignViewsRequest request) throws Exception {
    return securityProfileService.assignViews(id,request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/security-profiles/{id}/endpoints")
  public SecurityProfile assignAuthorizedEndpoint(@PathVariable(value = "id") Long id,
      @RequestBody AssignEndpointsRequest request) throws Exception {
    return securityProfileService.assignEndpoints(id,request);
  }
}
