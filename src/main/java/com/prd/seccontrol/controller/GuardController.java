package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateGuardRequest;
import com.prd.seccontrol.model.dto.GuardDto;
import com.prd.seccontrol.model.dto.GuardLiteView;
import com.prd.seccontrol.model.entity.Guard;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.service.impl.ShiftFilterService;
import com.prd.seccontrol.service.inter.SearchService;
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
public class GuardController {

  @Autowired
  private GuardRepository guardRepository;

  @Autowired
  private SearchService<Guard> searchService;

  @Autowired
  private ShiftFilterService shiftFilterService;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard/all")
  public Page<GuardDto> findAll(@RequestParam(required = false) String query,  Pageable pageable) {
    return searchService.search(query, pageable, Guard.class).map(GuardDto::new);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard/{id}")
  public Guard findById(@PathVariable Long id){
    return guardRepository.findById(id).get();
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard")
  public GuardDto createGuard(@RequestBody CreateGuardRequest request) {
    Guard guard = new Guard();
    guard.setEmployeeId(request.employeeId());
    guard.setLicenseNumber(request.licenseNumber());
    guard.setGuardType(request.guardType());
    guard.setPhotoUrl(request.photoUrl());
    guard.setCode(request.code());
    return new GuardDto(guardRepository.save(guard));
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard/{id}")
  public GuardDto updateGuard(@RequestBody CreateGuardRequest request, @PathVariable Long id) throws Exception {
    Guard guard = guardRepository.findById(id)
        .orElseThrow(() -> new Exception("Guard not found with id: " + id));
    guard.setEmployeeId(
        request.employeeId() != null ? request.employeeId() : guard.getEmployeeId());
    guard.setLicenseNumber(
        request.licenseNumber() != null ? request.licenseNumber() : guard.getLicenseNumber());
    guard.setGuardType(request.guardType() != null ? request.guardType() : guard.getGuardType());
    guard.setPhotoUrl(request.photoUrl() != null ? request.photoUrl() : guard.getPhotoUrl());
    guard.setCode(request.code() != null ? request.code() : guard.getCode());
    return new GuardDto(guardRepository.save(guard));
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard/{id}")
  public Long deleteGuard(@PathVariable Long id) {
    guardRepository.deleteById(id);
    return id;
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard/lite-search")
  public Page<GuardLiteView> findGeneralGuards(@RequestParam String searchTerm, Pageable pageable) {
    return shiftFilterService.findGeneralGuards(searchTerm ,pageable);
  }

}
