package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.AssignEndpointsRequest;
import com.prd.seccontrol.model.dto.AssignViewsRequest;
import com.prd.seccontrol.model.dto.CreateSecurityProfileRequest;
import com.prd.seccontrol.model.dto.UpdateSecurityProfileRequest;
import com.prd.seccontrol.model.entity.AuthorizedEndpoint;
import com.prd.seccontrol.model.entity.SecurityProfile;
import com.prd.seccontrol.model.entity.ViewAuthorization;
import com.prd.seccontrol.repository.AuthorizedEndpointRepository;
import com.prd.seccontrol.repository.SecurityProfileRepository;
import com.prd.seccontrol.repository.ViewAuthorizationRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityProfileService {

  @Autowired
  private SecurityProfileRepository securityProfileRepository;

  @Autowired
  private ViewAuthorizationRepository viewAuthorizationRepository;

  @Autowired
  private AuthorizedEndpointRepository authorizedEndpointRepository;

  public SecurityProfile create(CreateSecurityProfileRequest request) {
    SecurityProfile securityProfile = new SecurityProfile();
    securityProfile.setName(request.name());
    securityProfile.setDescription(request.description());
    return securityProfileRepository.save(securityProfile);
  }

  public SecurityProfile update(Long Id, UpdateSecurityProfileRequest request) {
    SecurityProfile securityProfile = securityProfileRepository.findById(Id)
        .orElseThrow(() -> new RuntimeException("Security Profile not found with id: " + Id));
    securityProfile.setName(request.name());
    securityProfile.setDescription(request.description());
    return securityProfileRepository.save(securityProfile);
  }

  @Transactional
  public SecurityProfile assignViews(Long profileId, AssignViewsRequest request) {

    List<ViewAuthorization> existing =
        viewAuthorizationRepository
            .findBySecurityProfileIdAndViewIdIn(profileId, request.viewIds());

    Set<Long> existingViewIds = new HashSet<>();
    Set<Long> existingAuthIds = new HashSet<>();

    for (ViewAuthorization va : existing) {
      existingViewIds.add(va.getViewId());
      existingAuthIds.add(va.getId());
    }

    if (existingAuthIds.isEmpty()) {
      viewAuthorizationRepository.deleteBySecurityProfileId(profileId);
    } else {
      viewAuthorizationRepository
          .deleteBySecurityProfileIdAndIdNotIn(profileId, existingAuthIds);
    }

    List<ViewAuthorization> toSave = request.viewIds().stream()
        .filter(id -> !existingViewIds.contains(id))
        .map(id -> {
          ViewAuthorization va = new ViewAuthorization();
          va.setSecurityProfileId(profileId);
          va.setViewId(id);
          return va;
        })
        .toList();

    viewAuthorizationRepository.saveAll(toSave);

    return securityProfileRepository.findById(profileId)
        .orElseThrow(() ->
            new RuntimeException("Security Profile not found with id: " + profileId));
  }

  @Transactional
  public SecurityProfile assignEndpoints(Long profileId, AssignEndpointsRequest request) {

    List<AuthorizedEndpoint> existing =
        authorizedEndpointRepository
            .findBySecurityProfileIdAndEndpointIdIn(profileId, request.endpointIds());

    Set<Long> existingEndpointIds = new HashSet<>();
    Set<Long> existingAuthIds = new HashSet<>();

    for (AuthorizedEndpoint au : existing) {
      existingEndpointIds.add(au.getEndpointId());
      existingAuthIds.add(au.getId());
    }

    if (existingAuthIds.isEmpty()) {
      authorizedEndpointRepository.deleteBySecurityProfileId(profileId);
    } else {
      authorizedEndpointRepository
          .deleteBySecurityProfileIdAndIdNotIn(profileId, existingAuthIds);
    }

    List<AuthorizedEndpoint> toSave = request.endpointIds().stream()
        .filter(id -> !existingEndpointIds.contains(id))
        .map(id -> {
          AuthorizedEndpoint va = new AuthorizedEndpoint();
          va.setSecurityProfileId(profileId);
          va.setEndpointId(id);
          return va;
        })
        .toList();

    authorizedEndpointRepository.saveAll(toSave);

    return securityProfileRepository.findById(profileId)
        .orElseThrow(() ->
            new RuntimeException("Security Profile not found with id: " + profileId));
  }
}
