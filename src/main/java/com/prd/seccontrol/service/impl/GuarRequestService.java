package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.dto.CreateLateJustificationRequest;
import com.prd.seccontrol.model.dto.GuardRequestDto;
import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.entity.GuardRequest;
import com.prd.seccontrol.model.types.RequestStatus;
import com.prd.seccontrol.model.types.RequestType;
import com.prd.seccontrol.repository.GuardAssistanceEventRepository;
import com.prd.seccontrol.repository.GuardRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuarRequestService {

  @Autowired
  private GuardRequestRepository guardRequestRepository;
  @Autowired
  private GuardAssistanceEventRepository guardAssistanceEventRepository;

  @Transactional
  public GuardRequestDto justificatedLateEvent(CreateLateJustificationRequest request) {
    GuardAssistanceEvent lateEvent = guardAssistanceEventRepository.findById(request.guardAssistanceEventId())
        .orElseThrow(() -> new RuntimeException("Assistance event not found"));

    GuardRequest guardRequest = new GuardRequest();
    guardRequest.setGuardAssistanceEventId(lateEvent.getId());
    guardRequest.setDescription(request.description());
    guardRequest.setGuardAssignmentId(lateEvent.getGuardAssignment().getId());
    guardRequest.setRequestType(RequestType.LATE_JUSTIFICATION);
    guardRequest.setRequestStatus(RequestStatus.PENDING);

    GuardRequest savedRequest = guardRequestRepository.save(guardRequest);
    return new GuardRequestDto(savedRequest, lateEvent.getAssistanceType());

  }
}
