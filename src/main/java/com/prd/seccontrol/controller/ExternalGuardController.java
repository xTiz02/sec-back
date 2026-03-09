package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateExternalGuardRequest;
import com.prd.seccontrol.model.dto.ExternalGuardDto;
import com.prd.seccontrol.model.entity.ExternalGuard;
import com.prd.seccontrol.repository.ExternalGuardRepository;
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
public class ExternalGuardController {

  @Autowired
  private SearchService<ExternalGuard> externalGuardSearchService;

  @Autowired
  private ExternalGuardRepository externalGuardRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/external-guard/all")
  public Page<ExternalGuardDto> getAllExternalGuards(@RequestParam(required = false) String query,
      Pageable pageable) {
    return externalGuardSearchService.search(query, pageable, ExternalGuard.class).map(
        ExternalGuardDto::new
    );
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/external-guard/{externalGuardId}")
  public ExternalGuardDto getExternalGuardById(@PathVariable Long externalGuardId) {
    ExternalGuard externalGuard = externalGuardRepository.findById(externalGuardId)
        .orElseThrow(
            () -> new RuntimeException("External Guard not found with id: " + externalGuardId));
    return new ExternalGuardDto(externalGuard);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/external-guard")
  public ExternalGuardDto createExternalGuard(@RequestBody CreateExternalGuardRequest request) {
    ExternalGuard externalGuard = new ExternalGuard();
    externalGuard.setFirstName(request.firstName());
    externalGuard.setLastName(request.lastName());
    externalGuard.setDocumentNumber(request.documentNumber());
    externalGuard.setEmail(request.email());
    externalGuard.setMobilePhone(request.mobilePhone());
    externalGuard.setGender(request.gender());
    externalGuard.setIdentificationType(request.identificationType());
    externalGuard.setCountry(request.country());
    externalGuard.setBusinessName(request.businessName());
    externalGuard.setBirthDate(request.birthDate());
    externalGuard.setActive(true);
    ExternalGuard savedExternalGuard = externalGuardRepository.save(externalGuard);
    return new ExternalGuardDto(savedExternalGuard);
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/external-guard/{externalGuardId}")
  public ExternalGuardDto updateExternalGuard(@PathVariable Long externalGuardId,
      @RequestBody CreateExternalGuardRequest request) {
    ExternalGuard externalGuard = externalGuardRepository.findById(externalGuardId)
        .orElseThrow(
            () -> new RuntimeException("External Guard not found with id: " + externalGuardId));
    externalGuard.setFirstName(request.firstName());
    externalGuard.setLastName(request.lastName());
    externalGuard.setDocumentNumber(request.documentNumber());
    externalGuard.setEmail(request.email());
    externalGuard.setMobilePhone(request.mobilePhone());
    externalGuard.setGender(request.gender());
    externalGuard.setIdentificationType(request.identificationType());
    externalGuard.setCountry(request.country());
    externalGuard.setBusinessName(request.businessName());
    externalGuard.setBirthDate(request.birthDate());
    ExternalGuard updatedExternalGuard = externalGuardRepository.save(externalGuard);
    return new ExternalGuardDto(updatedExternalGuard);
  }

  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/external-guard/{externalGuardId}")
  public Long deleteExternalGuard(@PathVariable Long externalGuardId) {
    ExternalGuard externalGuard = externalGuardRepository.findById(externalGuardId)
        .orElseThrow(
            () -> new RuntimeException("External Guard not found with id: " + externalGuardId));
    externalGuardRepository.delete(externalGuard);
    return externalGuardId;
  }
}
