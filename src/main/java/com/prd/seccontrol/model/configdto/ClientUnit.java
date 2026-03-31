package com.prd.seccontrol.model.configdto;

import java.util.List;

public class ClientUnit {

  private String uCode;
  private String uName;

  /**
   * Un GuardSchedule por cada guardia único que aparece en esta unidad. Ya están consolidadas las
   * filas repetidas del mismo guardia.
   */
  private List<GuardSchedule> guards;

  public ClientUnit() {
  }

  public ClientUnit(String uCode, String uName, List<GuardSchedule> guards) {
    this.uCode = uCode;
    this.uName = uName;
    this.guards = guards;
  }

  public String getUCode() {
    return uCode;
  }

  public void setUCode(String v) {
    this.uCode = v;
  }

  public String getUName() {
    return uName;
  }

  public void setUName(String v) {
    this.uName = v;
  }

  public List<GuardSchedule> getGuards() {
    return guards;
  }

  public void setGuards(List<GuardSchedule> v) {
    this.guards = v;
  }

  @Override
  public String toString() {
    return "ClientUnit{uCode='" + uCode + "', uName='" + uName +
        "', guardias=" + (guards != null ? guards.size() : 0) + '}';
  }
}
