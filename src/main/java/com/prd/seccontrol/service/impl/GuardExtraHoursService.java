package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateExtraHoursRequest;
import com.prd.seccontrol.model.entity.GuardExtraHours;
import com.prd.seccontrol.model.entity.User;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.GuardExtraHoursRepository;
import com.prd.seccontrol.repository.UserRepository;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuardExtraHoursService {

  @Autowired
  private GuardExtraHoursRepository guardExtraHoursRepository;
  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;
  @Autowired
  private UserRepository userRepository;


  @Transactional
  public void assignGuardToExtraHourShift(CreateExtraHoursRequest request, Principal principal) {
    String username = principal.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    GuardExtraHours guardExtraHours = new GuardExtraHours();
    guardExtraHours.setCoverDateGuardUnityAssignmentId(request.coverDateGuardUnityAssignmentId());
    guardExtraHours.setPrincipalDateGuardUnityAssignmentId(
        request.principalDateGuardUnityAssignmentId());
    guardExtraHours.setStartDate(request.startDate());
    guardExtraHours.setStartTime(request.startTime());
    guardExtraHours.setEndDate(request.endDate());
    guardExtraHours.setEndTime(request.endTime());
    guardExtraHours.setOperatorUserId(user.getId());

    if (request.startDate() != null && request.endDate() != null) {
      guardExtraHours.setExtraHours((int)
          Duration.between(LocalDateTime.of(request.endDate(), request.endTime()),
              LocalDateTime.of(request.startDate(), request.startTime())).toMinutes());
    }

    guardExtraHoursRepository.save(guardExtraHours);
    dateGuardUnityAssignmentRepository.updateHasExtraHoursById(
        request.principalDateGuardUnityAssignmentId(), true,
        false); // todo definir cuando termina un turno al crear horas extra.
  }
}
