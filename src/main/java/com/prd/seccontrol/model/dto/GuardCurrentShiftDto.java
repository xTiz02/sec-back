package com.prd.seccontrol.model.dto;

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
      List<GuardAssistanceEventDto> todayEvents,
      GuardExtraHoursDto activeExtraHours,
      List<GuardRequestDto> lateRequests)  {
    this(
        info.guardName(),
        info.guardDocumentNumber(),
        info.guardPhotoUrl(),
        info.guardType() != null && info.guardType().equals(GuardType.ROTATING),
        new GuardShiftDto(
            info.dateGuardUnityAssignmentId(),
            info.date(),
            new ContractUnityInfo(
                info.contractUnityId(),
                info.unityName(),
                info.address(),
                info.latitude(),
                info.longitude(),
                info.allowedRadius()
            ),
            info.turnTemplate()
        ),
        todayEvents,
        activeExtraHours,
        lateRequests
    );

  }

  public GuardCurrentShiftDto(){
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
