package com.prd.seccontrol.model.types;

public enum IdentificationType {
  DNI, CE, PASSPORT, OTHER;

  public static IdentificationType fromOrdinal(int ordinal) {
    return values()[ordinal];
  }
}
