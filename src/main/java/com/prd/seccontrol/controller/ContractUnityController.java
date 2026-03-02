package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.ContractUnityDto;
import com.prd.seccontrol.repository.ContractUnityRepository;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractUnityController {

  @Autowired
  private ContractUnityRepository contractUnityRepository;


  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-unity/by-contract/{contractId}")
  public List<ContractUnityDto> findContractUnities(@PathVariable Long contractId) {
    return contractUnityRepository.findByClientContractId(contractId).stream().map(ContractUnityDto::new).toList();
  }
}
