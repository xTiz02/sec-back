package com.prd.seccontrol.model.types;

public enum GuardType {
  HOLDER, RELEASE, ROTATING, BASE_AGENT;

  public static GuardType fromOrdinal(int ordinal) {
    return values()[ordinal];
  }
}
