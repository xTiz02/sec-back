package com.prd.seccontrol.model.configdto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleRow {

  private String viCode; // Código del guardia
  private String viName; // Nombre del guardia
  private String uCode;  // Unidad a la que pertenece ESTA fila
  private String uName;

  /**
   * Fila del Excel donde está este registro (1-indexed). Permite ubicar exactamente el origen de
   * cada dato.
   */
  private int excelRowNumber;

  /**
   * Turnos de esta fila. Cada ShiftCell contiene: día, valor del turno y posición exacta en el
   * Excel. Solo se incluyen celdas con turno válido (D, N, X, VC).
   */
  private List<ShiftCell> shifts;

  public ScheduleRow() {
  }

  public ScheduleRow(String viCode, String viName,
      String uCode, String uName,
      int excelRowNumber, List<ShiftCell> shifts) {
    this.viCode = viCode;
    this.viName = viName;
    this.uCode = uCode;
    this.uName = uName;
    this.excelRowNumber = excelRowNumber;
    this.shifts = shifts;
  }

  /**
   * Turno para un día específico, o null si esta fila no tiene asignación ese día.
   */
  public String getShiftForDay(int day) {
    if (shifts == null) {
      return null;
    }
    return shifts.stream()
        .filter(s -> s.getDay() == day)
        .map(ShiftCell::getShift)
        .findFirst()
        .orElse(null);
  }

  /**
   * ShiftCell completa para un día específico, o null si no existe.
   */
  public ShiftCell getShiftCellForDay(int day) {
    if (shifts == null) {
      return null;
    }
    return shifts.stream()
        .filter(s -> s.getDay() == day)
        .findFirst()
        .orElse(null);
  }

  /**
   * Mapa día → turno (solo los días con asignación). Útil para iterar rápido.
   */
  public Map<Integer, String> getDailyShiftsMap() {
    if (shifts == null) {
      return Map.of();
    }
    return shifts.stream()
        .collect(Collectors.toMap(ShiftCell::getDay, ShiftCell::getShift));
  }

  /**
   * Campos auxiliares para transportar cliCode/cliName desde el parser. No forman parte de la
   * lógica de negocio de ScheduleRow (el cliente es único por archivo y vive en MonthlySchedule).
   */
  private String excelCliCode;
  private String excelCliName;

  public String getExcelCliCode() {
    return excelCliCode;
  }

  public void setExcelCliCode(String v) {
    this.excelCliCode = v;
  }

  public String getExcelCliName() {
    return excelCliName;
  }

  public void setExcelCliName(String v) {
    this.excelCliName = v;
  }

  public String getViCode() {
    return viCode;
  }

  public void setViCode(String v) {
    this.viCode = v;
  }

  public String getViName() {
    return viName;
  }

  public void setViName(String v) {
    this.viName = v;
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

  public int getExcelRowNumber() {
    return excelRowNumber;
  }

  public void setExcelRowNumber(int v) {
    this.excelRowNumber = v;
  }

  public List<ShiftCell> getShifts() {
    return shifts;
  }

  public void setShifts(List<ShiftCell> v) {
    this.shifts = v;
  }

  @Override
  public String toString() {
    return "ScheduleRow{excelRow=" + excelRowNumber +
        ", vi=" + viCode + " (" + viName + ")" +
        ", uCode=" + uCode +
        ", turnos=" + (shifts != null ? shifts.size() : 0) + '}';
  }
}
