package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.AssignTurnsToWeekRequest;
import com.prd.seccontrol.model.dto.DayScheduleSummaryDto;
import com.prd.seccontrol.model.dto.DayTurnAssignment;
import com.prd.seccontrol.model.dto.UnitWeeklySchedule;
import com.prd.seccontrol.model.dto.WeeklyScheduleSummaryDto;
import com.prd.seccontrol.model.entity.ClientContract;
import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import com.prd.seccontrol.model.entity.ContractUnity;
import com.prd.seccontrol.model.entity.TurnAndHour;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.TurnType;
import com.prd.seccontrol.repository.ClientContractRepository;
import com.prd.seccontrol.repository.ContractScheduleTemplateRepository;
import com.prd.seccontrol.repository.ContractUnityRepository;
import com.prd.seccontrol.repository.TurnAndHourRepository;
import com.prd.seccontrol.repository.TurnTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractScheduleService {

  @Autowired
  private ContractScheduleTemplateRepository contractScheduleTemplateRepository;

  @Autowired
  private ContractUnityRepository contractUnityRepository;

  @Autowired
  private TurnAndHourRepository turnAndHourRepository;

  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

  @Autowired
  private ClientContractRepository clientContractRepository;

  public List<WeeklyScheduleSummaryDto> getContractScheduleByContractUnityId(
      Long clientContractId) {
    List<WeeklyScheduleSummaryDto> summaries = new ArrayList<>();

    List<ContractUnity> contractUnities = contractUnityRepository.findByClientContractId(
        clientContractId);
    if (contractUnities.isEmpty()) {
      return summaries;
    }

    for (ContractUnity contractUnity : contractUnities) {

      List<Long> scheduleIds = contractScheduleTemplateRepository.findContractScheduleIdByContractUnityId(
          contractUnity.getId());

      List<TurnAndHour> turnAndHours = turnAndHourRepository.findByContractScheduleUnitTemplateIdIn(
          scheduleIds);

      // group by contractScheduleUnitTemplateId with turnTemplate list
      Map<Long, List<TurnTemplate>> scheduleMap = turnAndHours.stream()
          .collect(Collectors.groupingBy(
              TurnAndHour::getContractScheduleUnitTemplateId,
              Collectors.mapping(TurnAndHour::getTurnTemplate, Collectors.toList())
          ));

      List<DayScheduleSummaryDto> daySchedules = new ArrayList<>();

      for (Map.Entry<Long, List<TurnTemplate>> entry : scheduleMap.entrySet()) {
        Long scheduleId = entry.getKey();
        List<TurnTemplate> turns = entry.getValue();

        ContractScheduleUnitTemplate scheduleUnitTemplate = turnAndHours.stream()
            .filter(t -> t.getContractScheduleUnitTemplateId().equals(scheduleId))
            .findFirst()
            .map(TurnAndHour::getContractScheduleUnitTemplate)
            .orElseThrow(
                () -> new RuntimeException(
                    "Schedule Unit Template not found for ID: " + scheduleId));

        DayScheduleSummaryDto daySchedule = new DayScheduleSummaryDto(scheduleUnitTemplate, turns);
        daySchedules.add(daySchedule);
      }

      WeeklyScheduleSummaryDto summary = new WeeklyScheduleSummaryDto(
          contractUnity.getId(),
          contractUnity.getUnityId(),
          contractUnity.getUnity().getCode(),
          contractUnity.getUnity().getName(),
          daySchedules
      );

      summaries.add(summary);
    }

    return summaries;
  }

  @Transactional
  public void assignTurnToScheduleUnit(AssignTurnsToWeekRequest request) {
    ClientContract clientContract = clientContractRepository.findById(request.contractId())
        .orElseThrow(() -> new RuntimeException(
            "Client Contract not found with id: " + request.contractId()));

    List<UnitWeeklySchedule> units = request.units();
    List<ContractUnity> existingContractUnities = contractUnityRepository.findByClientContractId(clientContract.getId());

    // delete contract unity that are not in the request
    List<Long> requestContractUnityIds = units.stream()
        .filter(u -> u.contractUnityId() != null)
        .map(UnitWeeklySchedule::contractUnityId)
        .toList();

    List<Long> existingContractUnityIds = existingContractUnities.stream()
        .map(ContractUnity::getId)
        .toList();

    List<Long> contractUnitiesToDelete = existingContractUnityIds.stream()
        .filter(id -> !requestContractUnityIds.contains(id))
        .toList();

    for (Long contractUnityId : contractUnitiesToDelete) {
      deleteContractUnit(contractUnityId);
    }

    for (UnitWeeklySchedule requestUnit : units) {
      validateDaysOfWeek(requestUnit.days());
      // check if contract unity exists for add or update schedule
      if (requestUnit.contractUnityId() != null) {
        List<Long> scheduleIds = contractScheduleTemplateRepository.findContractScheduleIdByContractUnityId(
            requestUnit.contractUnityId());

        List<TurnAndHour> turnAndHours = turnAndHourRepository.findByContractScheduleUnitTemplateIdIn(
            scheduleIds);

        List<ContractScheduleUnitTemplate> scheduleUnits = new ArrayList<>(turnAndHours.stream()
            .map(TurnAndHour::getContractScheduleUnitTemplate)
            .distinct()
            .toList());

        List<ContractScheduleUnitTemplate> requestNewScheduleUnits = requestUnit.days().stream()
            .filter(d -> scheduleUnits.stream().noneMatch(s -> s.getDayOfWeek() == d.dayOfWeek()))
            .map(d -> {
              ContractScheduleUnitTemplate c = new ContractScheduleUnitTemplate();
              c.setContractUnityId(requestUnit.contractUnityId());
              c.setDayOfWeek(d.dayOfWeek());

              c = contractScheduleTemplateRepository.save(c);
              return c;
            })
            .toList();

        scheduleUnits.addAll(requestNewScheduleUnits);

        // verify day of week TurnTemplates
        for (ContractScheduleUnitTemplate scheduleUnit : scheduleUnits) {
          DayOfWeek dayOfWeek = scheduleUnit.getDayOfWeek();
          List<TurnAndHour> turnAndHourList = turnAndHours.stream()
              .filter(t -> t.getContractScheduleUnitTemplateId().equals(scheduleUnit.getId()))
              .toList();

          List<Long> requestTurnTemplateIds = requestUnit.days().stream()
              .filter(d -> d.dayOfWeek().equals(dayOfWeek))
              .flatMap(d -> d.turnTemplateIds().stream())
              .toList();

          List<Long> existingTurnAndHourIds =
              turnAndHourList.stream().filter(
                      t -> requestTurnTemplateIds.contains(t.getTurnTemplateId()))
                  .map(TurnAndHour::getId)
                  .toList();

          List<Long> existingTurnTemplateIds =
              turnAndHourList.stream().filter(
                      t -> requestTurnTemplateIds.contains(t.getTurnTemplateId()))
                  .map(TurnAndHour::getTurnTemplateId)
                  .toList();

          List<TurnAndHour> toSave = new ArrayList<>();

          if (!turnAndHourList.isEmpty()) {
            if (existingTurnAndHourIds.isEmpty()) {
              turnAndHourRepository.deleteByContractScheduleUnitTemplateId(scheduleUnit.getId());
              contractScheduleTemplateRepository.deleteById(scheduleUnit.getId());
              verifyAndDeleteIfContractUnityEmpty(requestUnit.contractUnityId());
            } else {
              turnAndHourRepository
                  .deleteByContractScheduleUnitTemplateIdAndIdNotIn(scheduleUnit.getId(),
                      existingTurnAndHourIds);
            }

            toSave = requestTurnTemplateIds.stream()
                .filter(id -> !existingTurnTemplateIds.contains(id))
                .map(id -> {
                  TurnAndHour ta = new TurnAndHour();
                  ta.setContractScheduleUnitTemplateId(scheduleUnit.getId());
                  ta.setTurnTemplateId(id);
                  return ta;
                })
                .toList();
          }  else {
            toSave = requestTurnTemplateIds.stream()
                .map(id -> {
                  TurnAndHour ta = new TurnAndHour();
                  ta.setContractScheduleUnitTemplateId(scheduleUnit.getId());
                  ta.setTurnTemplateId(id);
                  return ta;
                })
                .toList();
          }

          if(!toSave.isEmpty()) {
            turnAndHourRepository.saveAll(toSave);
            updateContractUnitTemplateFields(scheduleUnit);
          }
        }
      } else {
        ContractUnity contractUnity = new ContractUnity();
        contractUnity.setClientContractId(clientContract.getId());
        contractUnity.setUnityId(requestUnit.unityId());

        ContractUnity savedContractUnity = contractUnityRepository.save(contractUnity);
        List<ContractScheduleUnitTemplate> scheduleUnitsToSave = new ArrayList<>();
        for (DayTurnAssignment dayTurn : requestUnit.days()) {
          ContractScheduleUnitTemplate scheduleUnit = new ContractScheduleUnitTemplate();
          scheduleUnit.setContractUnityId(savedContractUnity.getId());
          scheduleUnit.setDayOfWeek(dayTurn.dayOfWeek());
          scheduleUnitsToSave.add(scheduleUnit);
        }
        List<ContractScheduleUnitTemplate> savedScheduleUnits = contractScheduleTemplateRepository.saveAll(
            scheduleUnitsToSave);
        List<TurnAndHour> turnAndHoursToSave = new ArrayList<>();

        for (int i = 0; i < savedScheduleUnits.size(); i++) {
          ContractScheduleUnitTemplate savedUnit = savedScheduleUnits.get(i);
          DayTurnAssignment dayTurn = requestUnit.days().get(i);
          for (Long turnTemplateId : dayTurn.turnTemplateIds()) {
            TurnAndHour turnAndHour = new TurnAndHour();
            turnAndHour.setContractScheduleUnitTemplateId(savedUnit.getId());
            turnAndHour.setTurnTemplateId(turnTemplateId);
            turnAndHoursToSave.add(turnAndHour);
          }
        }

        turnAndHourRepository.saveAll(turnAndHoursToSave);
        savedScheduleUnits.forEach(this::updateContractUnitTemplateFields);

      }
    }
  }

  public void verifyAndDeleteIfContractUnityEmpty(Long contractUnityId) {
    List<Long> scheduleIds = contractScheduleTemplateRepository.findContractScheduleIdByContractUnityId(
        contractUnityId);

    if (scheduleIds.isEmpty()) {
      contractUnityRepository.deleteById(contractUnityId);
    }
  }

  public void deleteContractUnit(Long contractUnitId) {
    List<Long> scheduleIds = contractScheduleTemplateRepository.findContractScheduleIdByContractUnityId(
        contractUnitId);

    if (!scheduleIds.isEmpty()) {
      turnAndHourRepository.deleteByContractScheduleUnitTemplateIdIn(scheduleIds);
      contractScheduleTemplateRepository.deleteByContractUnityId(contractUnitId);
    }
    contractUnityRepository.deleteById(contractUnitId);
  }

  public void updateContractUnitTemplateFields(ContractScheduleUnitTemplate scheduleUnit) {
    List<TurnTemplate> turnAndHours = turnTemplateRepository.findTurnTemplatesByContractUnitTemplateId(
        scheduleUnit.getId());

    int totalGuardsAssigned = turnAndHours.stream()
        .mapToInt(t -> t.getNumGuards())
        .sum();
    int totalTurnDaysAssigned = turnAndHours.stream()
        .filter(t -> t.getTurnType() == TurnType.DAY)
        .mapToInt(t -> t.getNumGuards())
        .sum();
    int totalTurnNightsAssigned = turnAndHours.stream()
        .filter(t -> t.getTurnType() == TurnType.NIGHT)
        .mapToInt(t -> t.getNumGuards())
        .sum();

    scheduleUnit.setNumOfGuards(totalGuardsAssigned);
    scheduleUnit.setNumTurnDay(totalTurnDaysAssigned);
    scheduleUnit.setNumTurnNight(totalTurnNightsAssigned);
    contractScheduleTemplateRepository.save(scheduleUnit);
  }

  public void validateDaysOfWeek(List<DayTurnAssignment> assignments) {
    List<DayOfWeek> days = assignments.stream().map(DayTurnAssignment::dayOfWeek).toList();
    // only allow unique days of week
    if (days.size() != days.stream().distinct().count()) {
      throw new RuntimeException("Duplicate days of week are not allowed");
    }
  }
}
