package com.prd.seccontrol.model.configdto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GuardSchedule {


  private String viCode;
  private String viName;
  private String uCode;
  private String uName;

  /**
   * Filas Excel originales que forman este guardia+unidad. Para auditoría.
   */
  private List<ScheduleRow> sourceRows;

  /**
   * Mapa consolidado día → ShiftCell, ordenado por día (TreeMap). Días sin turno no aparecen en el
   * mapa.
   */
  private Map<Integer, ShiftCell> mergedShifts;

  /**
   * Rangos consecutivos de días VC calculados sobre mergedShifts. Se recalcula cada vez que cambia
   * mergedShifts.
   */
  private List<VacationRange> vacationRanges;

  public GuardSchedule() {
  }

  public GuardSchedule(String viCode, String viName,
      String uCode, String uName,
      List<ScheduleRow> sourceRows) {
    this.viCode = viCode;
    this.viName = viName;
    this.uCode = uCode;
    this.uName = uName;
    this.sourceRows = sourceRows;
    this.mergedShifts = mergeShifts(sourceRows);
    this.vacationRanges = computeVacationRanges(this.mergedShifts);
  }

  // ── Consultas rápidas ─────────────────────────────────────────

  /**
   * Turno para un día específico, o null si no hay asignación.
   */
  public String getShiftForDay(int day) {
    ShiftCell cell = mergedShifts.get(day);
    return cell != null ? cell.getShift() : null;
  }

  /**
   * ShiftCell completa (con posición Excel) para un día, o null.
   */
  public ShiftCell getShiftCellForDay(int day) {
    return mergedShifts.get(day);
  }

  /**
   * true si el guardia tiene al menos un período de vacaciones este mes.
   */
  public boolean hasVacations() {
    return !vacationRanges.isEmpty();
  }

  // ── Merge de filas ────────────────────────────────────────────

  private static Map<Integer, ShiftCell> mergeShifts(List<ScheduleRow> rows) {
    Map<Integer, ShiftCell> merged = new TreeMap<>(); // ordenado por día
    for (ScheduleRow row : rows) {
      if (row.getShifts() == null) {
        continue;
      }
      for (ShiftCell cell : row.getShifts()) {
        merged.putIfAbsent(cell.getDay(), cell); // gana la primera fila
      }
    }
    return merged;
  }

  // ── Cálculo de rangos VC ──────────────────────────────────────

  /**
   * Recorre el mapa de turnos (ya ordenado por día) y agrupa los días consecutivos con turno "VC"
   * en objetos VacationRange.
   * <p>
   * Regla: - Un rango VC comienza al encontrar el primer día con turno "VC". - El rango continúa
   * mientras los siguientes días también sean "VC" Y sean consecutivos. - El rango se cierra
   * cuando: a) el siguiente día tiene un turno distinto (D, N, X), o b) el siguiente día no es
   * consecutivo (hay un hueco de días sin asignación), o c) se acaba el mapa.
   * <p>
   * Ejemplo con mergedShifts = { 1:VC, 2:VC, ..., 15:VC, 16:D, 26:VC, 27:VC, ..., 31:VC } → [
   * VacationRange(1,15), VacationRange(26,31) ]
   * <p>
   * Ejemplo con un hueco: { 1:VC, 2:VC, 4:VC, 5:VC }  (día 3 en blanco) → [ VacationRange(1,2),
   * VacationRange(4,5) ] (el hueco rompe la consecutividad)
   */
  private static List<VacationRange> computeVacationRanges(Map<Integer, ShiftCell> shifts) {
    List<VacationRange> ranges = new ArrayList<>();
    if (shifts.isEmpty()) {
      return ranges;
    }

    Integer rangeStart = null; // día donde empezó el rango VC actual
    Integer prevDay = null; // día anterior procesado

    for (Map.Entry<Integer, ShiftCell> entry : shifts.entrySet()) {
      int day = entry.getKey();
      String shift = entry.getValue().getShift();

      boolean isVC = "VC".equals(shift);
      boolean isConsecutive = prevDay != null && day == prevDay + 1;

      if (isVC) {
        if (rangeStart == null) {
          // Inicia un nuevo rango
          rangeStart = day;
        } else if (!isConsecutive) {
          // Había un rango abierto pero hay un hueco → cerrar el anterior y abrir nuevo
          ranges.add(new VacationRange(rangeStart, prevDay));
          rangeStart = day;
        }
        // Si isVC && isConsecutive → el rango continúa, no hacemos nada
      } else {
        // Turno D, N o X → cierra el rango VC si estaba abierto
        if (rangeStart != null) {
          ranges.add(new VacationRange(rangeStart, prevDay));
          rangeStart = null;
        }
      }

      prevDay = day;
    }

    // Si el mapa termina con días VC, cerrar el último rango
    if (rangeStart != null) {
      ranges.add(new VacationRange(rangeStart, prevDay));
    }

    return ranges;
  }

  // ── Getters & Setters ─────────────────────────────────────────

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

  public List<ScheduleRow> getSourceRows() {
    return sourceRows;
  }

  public void setSourceRows(List<ScheduleRow> v) {
    this.sourceRows = v;
  }

  public Map<Integer, ShiftCell> getMergedShifts() {
    return mergedShifts;
  }

  public List<VacationRange> getVacationRanges() {
    return vacationRanges;
  }

  @Override
  public String toString() {
    return "GuardSchedule{vi=" + viCode + " (" + viName + ")" +
        ", uCode=" + uCode +
        ", diasConTurno=" + mergedShifts.size() +
        ", vacaciones=" + vacationRanges +
        ", filasFuente=" + sourceRows.size() + '}';
  }
}
