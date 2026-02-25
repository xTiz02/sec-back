package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.AssignTurnsToWeekRequest;
import com.prd.seccontrol.model.dto.ContractScheduleUnitTemplateDto;
import com.prd.seccontrol.model.dto.TurnTemplateDto;
import com.prd.seccontrol.model.dto.WeeklyScheduleSummaryDto;
import com.prd.seccontrol.repository.ContractScheduleTemplateRepository;
import com.prd.seccontrol.repository.TurnAndHourRepository;
import com.prd.seccontrol.service.impl.ContractScheduleService;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractScheduleController {
  @Autowired
  private ContractScheduleService contractScheduleService;

  @Autowired
  private ContractScheduleTemplateRepository contractScheduleTemplateRepository;

  @Autowired
  private TurnAndHourRepository turnAndHourRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-schedule/by-contract/{clientContractId}")
  public List<WeeklyScheduleSummaryDto> findContractScheduleByContractUnity(@PathVariable(value = "clientContractId") Long contractUnityId) {
    return contractScheduleService.getContractScheduleByContractUnityId(contractUnityId);
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-schedule/assign-weekly-turns")
  public void assignWeeklyTurnsToContractUnity(@RequestBody AssignTurnsToWeekRequest request) {
    contractScheduleService.assignTurnToScheduleUnit(request);
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-schedule/{contractUnityId}/summary")
  public List<ContractScheduleUnitTemplateDto> getContractScheduleSummary(@PathVariable Long contractUnityId) {
    return contractScheduleTemplateRepository.findByContractUnityId(contractUnityId)
        .stream()
        .map(ContractScheduleUnitTemplateDto::new)
        .toList();
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-schedule/{contractScheduleUnityId}")
  public List<TurnTemplateDto> getContractScheduleUnitTemplateById(@PathVariable Long contractScheduleUnityId) {
    return turnAndHourRepository.findByContractScheduleUnitTemplateId(contractScheduleUnityId)
        .stream()
        .map(TurnTemplateDto::new)
        .toList();
  }


}
