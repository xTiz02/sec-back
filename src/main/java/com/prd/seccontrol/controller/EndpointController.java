package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.entity.Endpoint;
import com.prd.seccontrol.repository.EndpointRepository;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {
  @Autowired
  private EndpointRepository endpointRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/endpoints/all")
  public List<Endpoint> findAll() {
    return endpointRepository.findAll();
  }
}
