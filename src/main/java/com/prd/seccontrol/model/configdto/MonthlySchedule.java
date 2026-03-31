package com.prd.seccontrol.model.configdto;

import java.time.YearMonth;
import java.util.List;

public class MonthlySchedule {

  private String contractCode;
  private YearMonth yearMonth;
  private int totalDays;
  private String cliCode;
  private String cliName;

  /**
   * Filas crudas del Excel en orden de aparición. Para auditoría o procesamiento alternativo.
   */
  private List<ScheduleRow> rawRows;

  /**
   * Unidades del cliente con sus guardias consolidados.
   */
  private List<ClientUnit> units;

  public MonthlySchedule() {
  }

  public MonthlySchedule(String contractCode,YearMonth yearMonth, int totalDays,
      String cliCode, String cliName,
      List<ScheduleRow> rawRows,
      List<ClientUnit> units) {
    this.contractCode = contractCode;
    this.yearMonth = yearMonth;
    this.totalDays = totalDays;
    this.cliCode = cliCode;
    this.cliName = cliName;
    this.rawRows = rawRows;
    this.units = units;

  }

  public YearMonth getYearMonth() {
    return yearMonth;
  }

  public void setYearMonth(YearMonth v) {
    this.yearMonth = v;
  }

  public int getTotalDays() {
    return totalDays;
  }

  public void setTotalDays(int v) {
    this.totalDays = v;
  }

  public String getCliCode() {
    return cliCode;
  }

  public void setCliCode(String v) {
    this.cliCode = v;
  }

  public String getCliName() {
    return cliName;
  }

  public void setCliName(String v) {
    this.cliName = v;
  }

  public List<ScheduleRow> getRawRows() {
    return rawRows;
  }

  public String getContractCode() {
    return contractCode;
  }

  public void setContractCode(String contractCode) {
    this.contractCode = contractCode;
  }

  public void setRawRows(List<ScheduleRow> v) {
    this.rawRows = v;
  }

  public List<ClientUnit> getUnits() {
    return units;
  }

  public void setUnits(List<ClientUnit> v) {
    this.units = v;
  }

  @Override
  public String toString() {
    return "MonthlySchedule{yearMonth=" + yearMonth +
        ", totalDays=" + totalDays +
        ", cliente=" + cliCode + " (" + cliName + ")" +
        ", unidades=" + (units != null ? units.size() : 0) +
        ", filasRaw=" + (rawRows != null ? rawRows.size() : 0) + '}';
  }
}
