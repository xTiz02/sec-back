package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateSpecialServiceUnityRequest;
import com.prd.seccontrol.model.entity.SpecialServiceUnity;
import com.prd.seccontrol.repository.SpecialServiceUnityRepository;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.util.SEConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialUnityController {

  @Autowired
  private SpecialServiceUnityRepository specialServiceUnityRepository;

  @Autowired
  private SearchService<SpecialServiceUnity> searchService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-unity/all")
  public Page<SpecialServiceUnity> getAllSpecialUnities(@RequestParam(required = false) String query, Pageable pageable) {
    return searchService.search(query, pageable, SpecialServiceUnity.class);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-unity/{id}")
  public SpecialServiceUnity getSpecialUnityById(@PathVariable Long id) {
    return specialServiceUnityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Special Service Unity not found with id: " + id));
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-unity")
  public SpecialServiceUnity createSpecialUnity(@RequestBody CreateSpecialServiceUnityRequest request) {
    SpecialServiceUnity specialServiceUnity = new SpecialServiceUnity();
    specialServiceUnity.setCode(request.code());
    specialServiceUnity.setUnityName(request.unityName());
    specialServiceUnity.setUnityDescription(request.unityDescription());
    specialServiceUnity.setAddress(request.address());
    specialServiceUnity.setActive(request.active());
    return specialServiceUnityRepository.save(specialServiceUnity);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-unity/{id}")
  public SpecialServiceUnity updateSpecialUnity(@RequestBody CreateSpecialServiceUnityRequest request, @PathVariable Long id) {
    SpecialServiceUnity specialServiceUnity = specialServiceUnityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Special Service Unity not found with id: " + id));
    specialServiceUnity.setCode(request.code());
    specialServiceUnity.setUnityName(request.unityName());
    specialServiceUnity.setUnityDescription(request.unityDescription());
    specialServiceUnity.setAddress(request.address());
    specialServiceUnity.setActive(request.active());
    return specialServiceUnityRepository.save(specialServiceUnity);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/special-service-unity/{id}")
  public Long deleteSpecialUnity(@PathVariable Long id) {
    SpecialServiceUnity specialServiceUnity = specialServiceUnityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Special Service Unity not found with id: " + id));
    specialServiceUnityRepository.delete(specialServiceUnity);
    return id;
  }
}
