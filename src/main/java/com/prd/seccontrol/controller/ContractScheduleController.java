package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.AssignTurnsToWeekRequest;
import com.prd.seccontrol.model.dto.ContractScheduleUnitTemplateDto;
import com.prd.seccontrol.model.dto.TurnAndHourDto;
import com.prd.seccontrol.model.dto.WeeklyScheduleSummaryDto;
import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import com.prd.seccontrol.repository.ContractScheduleTemplateRepository;
import com.prd.seccontrol.repository.TurnAndHourRepository;
import com.prd.seccontrol.repository.TurnTemplateRepository;
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

  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

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
    List<ContractScheduleUnitTemplate> list = contractScheduleTemplateRepository.findByContractUnityId(contractUnityId);
    List<Long> contractScheduleUnitIds = list.stream().map(ContractScheduleUnitTemplate::getId).toList();
    List<TurnAndHourDto> turnsAndHours = turnAndHourRepository.findByContractScheduleUnitTemplateIdIn(contractScheduleUnitIds)
        .stream()
        .map(TurnAndHourDto::new)
        .toList();

    return list.stream()
        .map(csut -> {
          List<TurnAndHourDto> turnsForUnit = turnsAndHours.stream()
              .filter(ta -> ta.contractScheduleUnitTemplateId().equals(csut.getId()))
              .toList();
          return new ContractScheduleUnitTemplateDto(csut, turnsForUnit);
        })
        .toList();
  }

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/contract-schedule/{contractScheduleUnityId}")
  public List<TurnAndHourDto> getContractScheduleUnitTemplateById(@PathVariable Long contractScheduleUnityId) {
    return turnAndHourRepository.findByContractScheduleUnitTemplateId(contractScheduleUnityId)
        .stream()
        .map(TurnAndHourDto::new)
        .toList();
  }


}
