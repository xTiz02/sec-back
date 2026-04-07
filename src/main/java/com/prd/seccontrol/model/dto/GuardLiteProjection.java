package com.prd.seccontrol.model.dto;

public interface GuardLiteProjection {
  Long getGuardId();
  Long getExternalGuardId();
  String getGuardCode();
  String getDocumentNumber();
  Integer getIdentificationType();
  String getGuardName();
  Integer getGuardType();
}
