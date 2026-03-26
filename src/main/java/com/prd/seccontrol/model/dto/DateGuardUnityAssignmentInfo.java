package com.prd.seccontrol.model.dto;

import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.GuardType;
import java.time.LocalDate;

public record DateGuardUnityAssignmentInfo(
    String guardName,
    String guardDocumentNumber,
    String guardPhotoUrl,
    GuardType guardType,
    Long dateGuardUnityAssignmentId,
    Long guardAssignmentId,
    Long guardUnityAssignmentId,
    LocalDate date,
    Long contractUnityId,
    String unityName,
    String address,
    Double latitude,
    Double longitude,
    Double allowedRadius,
    TurnTemplate turnTemplate,
    boolean hasException,
    boolean hasExtraHours,
    boolean finalized
) {

  public DateGuardUnityAssignmentInfo(
      String guardName,
      String guardDocumentNumber,
      String guardPhotoUrl,
      GuardType guardType,
      Long dateGuardUnityAssignmentId,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      LocalDate date,
      Long contractUnityId,
      String unityName,
      String address,
      Double latitude,
      Double longitude,
      Double allowedRadius,
      TurnTemplate turnTemplate,
      boolean hasException,
      boolean hasExtraHours,
      boolean finalized
  ) {
    this.guardName = guardName;
    this.guardDocumentNumber = guardDocumentNumber;
    this.guardPhotoUrl = guardPhotoUrl;
    this.guardType = guardType;
    this.dateGuardUnityAssignmentId = dateGuardUnityAssignmentId;
    this.guardAssignmentId = guardAssignmentId;
    this.guardUnityAssignmentId = guardUnityAssignmentId;
    this.date = date;
    this.contractUnityId = contractUnityId;
    this.unityName = unityName;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.allowedRadius = allowedRadius;
    this.turnTemplate = turnTemplate;
    this.hasException = hasException;
    this.hasExtraHours = hasExtraHours;
    this.finalized = finalized;
  }

  public DateGuardUnityAssignmentInfo(
      String guardName,
      String guardDocumentNumber,
      String guardPhotoUrl,
      GuardType guardType,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      DateGuardUnityAssignmentInfo other
  ) {
    this(guardName,
        guardDocumentNumber,
        guardPhotoUrl,
        guardType,
        other.dateGuardUnityAssignmentId,
        guardAssignmentId,
        guardUnityAssignmentId,
        other.date,
        other.contractUnityId,
        other.unityName,
        other.address,
        other.latitude,
        other.longitude,
        other.allowedRadius,
        other.turnTemplate,
        other.hasException,
        other.hasExtraHours,
        other.finalized);
  }

  public DateGuardUnityAssignmentInfo(
      GuardType guardType,
      Long dateGuardUnityAssignmentId,
      Long guardAssignmentId,
      Long guardUnityAssignmentId,
      TurnTemplate turnTemplate,
      boolean hasException,
      boolean hasExtraHours,
      boolean finalized
  ) {
    this(
        null,
        null,
        null,
        guardType,
        dateGuardUnityAssignmentId,
        guardAssignmentId,
        guardUnityAssignmentId,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        turnTemplate,
        hasException,
        hasExtraHours,
        finalized
    );
  }
}
