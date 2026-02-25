package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateUnityRequest;
import com.prd.seccontrol.model.dto.UnityDto;
import com.prd.seccontrol.model.entity.Unity;
import com.prd.seccontrol.repository.UnityRepository;
import com.prd.seccontrol.service.impl.SearchService;
import com.prd.seccontrol.service.impl.UnityService;
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
public class UnityController {

  @Autowired
  private UnityRepository unityRepository;

  @Autowired
  private SearchService<Unity> searchService;

  @Autowired
  private UnityService unityService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/unity/all")
  public Page<UnityDto> findAll(@RequestParam(required = false) String query, Pageable
      pageable) {
    return searchService.search(query, pageable, Unity.class).map(UnityDto::new);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/unity/{id}")
  public UnityDto findById(@PathVariable Long id) throws Exception {
    Unity unity = unityRepository.findById(id)
        .orElseThrow(() -> new Exception("Unity not found with id: " + id));
    return new UnityDto(unity);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/unity")
  public UnityDto createUnity(@RequestBody CreateUnityRequest request) {
    return unityService.createUnity(request);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/unity/{id}")
  public UnityDto updateUnity(@RequestBody CreateUnityRequest request, @PathVariable Long id) throws Exception {
    return unityService.updateUnity(id, request);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/unity/{id}")
  public Long deleteUnity(@PathVariable Long id) throws Exception {
    Unity unity = unityRepository.findById(id)
        .orElseThrow(() -> new Exception("Unity not found with id: " + id));
    unityRepository.delete(unity);
    return id;
  }
}
