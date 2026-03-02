package com.prd.seccontrol.model.types;

public enum DayOfWeek {
  MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

  public static DayOfWeek fromJavaDayOfWeek(java.time.DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> DayOfWeek.MONDAY;
      case TUESDAY -> DayOfWeek.TUESDAY;
      case WEDNESDAY -> DayOfWeek.WEDNESDAY;
      case THURSDAY -> DayOfWeek.THURSDAY;
      case FRIDAY -> DayOfWeek.FRIDAY;
      case SATURDAY -> DayOfWeek.SATURDAY;
      case SUNDAY -> DayOfWeek.SUNDAY;
    };
  }
}
