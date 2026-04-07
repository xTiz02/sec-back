package com.prd.seccontrol.model.types;


public enum ScheduleAssignmentType {
  NORMAL, ADDITIONAL, EXCEPTIONAL,VACATIONAL, FREE_DAY, ABSENT;

  public static ScheduleAssignmentType fromOrdinal(int ordinal) {
    return values()[ordinal];
  }
}
