package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateUnityRequest;
import com.prd.seccontrol.model.dto.UnityDto;
import com.prd.seccontrol.model.entity.Unity;
import com.prd.seccontrol.repository.UnityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnityService {

  @Autowired
  private UnityRepository unityRepository;


  public UnityDto createUnity(CreateUnityRequest request) {
    Unity unity = new Unity();
    unity.setClientId(request.clientId());
    unity.setCode(request.code());
    unity.setName(request.name());
    unity.setDescription(request.description());
    unity.setDirection(request.direction());
    unity.setLatitude(request.latitude());
    unity.setLongitude(request.longitude());
    unity.setRangeCoverage(request.rangeCoverage());
    unity.setActive(request.active());
    Unity savedUnity = unityRepository.save(unity);
    return new UnityDto(savedUnity);
  }

  public UnityDto updateUnity(Long id, CreateUnityRequest request) throws Exception {
    Unity unity = unityRepository.findById(id)
        .orElseThrow(() -> new Exception("Unity not found with id: " + id));
    unity.setClientId(request.clientId());
    unity.setCode(request.code() != null ? request.code() : unity.getCode());
    unity.setName(request.name() != null ? request.name() : unity.getName());
    unity.setDescription(request.description() != null ? request.description() : unity.getDescription());
    unity.setDirection(request.direction() != null ? request.direction() : unity.getDirection());
    unity.setLatitude(request.latitude() != null ? request.latitude() : unity.getLatitude());
    unity.setLongitude(request.longitude() != null ? request.longitude() : unity.getLongitude());
    unity.setRangeCoverage(request.rangeCoverage() != null ? request.rangeCoverage() : unity.getRangeCoverage());
    unity.setActive(request.active() != null ? request.active() : unity.isActive());
    Unity updatedUnity = unityRepository.save(unity);
    return new UnityDto(updatedUnity);
  }
}
