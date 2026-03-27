package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.types.GuardType;
import java.util.List;

public record GuardCurrentShiftDto(
    String guardName,
    String guardDocumentNumber,
    String guardPhotoUrl,
    boolean isDescansero,
    GuardShiftDto shift,
    List<GuardAssistanceEventDto> todayEvents,
    GuardExtraHoursDto activeExtraHours,
    List<GuardRequestDto> lateRequests
) {

  public GuardCurrentShiftDto(DateGuardUnityAssignmentInfo info,
      GuardUnityScheduleAssignment assignment,
      List<GuardAssistanceEventDto> todayEvents,
      GuardExtraHoursDto activeExtraHours,
      List<GuardRequestDto> lateRequests) {
    this(
        info.guardName(),
        info.guardDocumentNumber(),
        info.guardPhotoUrl(),
        info.guardType() != null && info.guardType().equals(GuardType.ROTATING),
        new GuardShiftDto(
            info.dateGuardUnityAssignmentId(),
            info.date(),
            assignment.getContractUnity() != null ? new ContractUnityInfo(
                 assignment.getContractUnity().getId(),
                assignment.getContractUnity().getUnity().getName(),
                assignment.getContractUnity().getUnity().getDirection(),
                assignment.getContractUnity().getUnity().getLatitude(),
                assignment.getContractUnity().getUnity().getLongitude(),
                assignment.getContractUnity().getUnity().getRangeCoverage()
            ) : null,
            assignment.getSpecialServiceUnitySchedule() != null
                ? assignment.getSpecialServiceUnitySchedule().getSpecialServiceUnity() : null,
            info.turnTemplate()
        ),
        todayEvents,
        activeExtraHours,
        lateRequests
    );

  }

  public GuardCurrentShiftDto() {
    this(
        null,
        null,
        null,
        false,
        null,
        List.of(),
        null,
        List.of()
    );
  }

}
