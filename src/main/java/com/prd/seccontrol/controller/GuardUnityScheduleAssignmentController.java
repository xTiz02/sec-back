package com.prd.seccontrol.controller;

import com.prd.seccontrol.model.dto.CreateGuardMonthlyAssignmentRequest;
import com.prd.seccontrol.model.dto.GuardUnityScheduleAssignmentDto;
import com.prd.seccontrol.model.dto.UpdateGuardUnityScheduleRequest;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.util.SEConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuardUnityScheduleAssignmentController {

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @GetMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-unity-schedule/by-contract-unity")
  public List<GuardUnityScheduleAssignmentDto> findByContractUnityAndMonthId(@RequestParam Long contractUnityId, @RequestParam Long scheduleMonthlyId) {
    return guardUnityScheduleAssignmentRepository.findByContractUnityIdAndScheduleMonthlyId(contractUnityId, scheduleMonthlyId)
        .stream()
        .map(GuardUnityScheduleAssignmentDto::new)
        .toList();
  }

  @Transactional
  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-unity-schedule")
  public GuardUnityScheduleAssignmentDto createGuardUnityScheduleAssignment(@RequestBody CreateGuardMonthlyAssignmentRequest request){
    GuardUnityScheduleAssignment duga = new GuardUnityScheduleAssignment();
    GuardAssignment ga = new GuardAssignment();
    ga.setGuardId(request.guardId());
    ga.setActive(true);
    ga = guardAssignmentRepository.save(ga);

    duga.setGuardAssignmentId(ga.getId());
    duga.setContractUnityId(request.contractUnityId());
    duga.setScheduleMonthlyId(request.scheduleMonthlyId());
    duga.setGuardType(request.guardType());

    duga = guardUnityScheduleAssignmentRepository.save(duga);
    return new GuardUnityScheduleAssignmentDto(duga);
  }

  @Transactional
  @DeleteMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-unity-schedule/{id}")
  public Long deleteGuardUnityScheduleAssignment(@PathVariable Long id) {
    dateGuardUnityAssignmentRepository.deleteByGuardUnityScheduleAssignmentId(id);
    guardUnityScheduleAssignmentRepository.deleteById(id);
    return id;
  }

  @PutMapping(SEConstants.SECURE_BASE_ENDPOINT + "/guard-unity-schedule/{id}")
  public GuardUnityScheduleAssignmentDto updateGuardUnityScheduleAssignment(@PathVariable Long id, @RequestBody UpdateGuardUnityScheduleRequest request){
    GuardUnityScheduleAssignment duga = guardUnityScheduleAssignmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("GuardUnityScheduleAssignment not found with id: " + id));
    duga.setGuardType(request.guardType());

    return new GuardUnityScheduleAssignmentDto(guardUnityScheduleAssignmentRepository.save(duga));
  }
}
