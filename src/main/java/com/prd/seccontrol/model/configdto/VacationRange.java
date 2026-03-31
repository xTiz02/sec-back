package com.prd.seccontrol.model.configdto;

public class VacationRange {

  /** Primer día del rango VC (1-indexed) */
  private int from;

  /** Último día del rango VC (1-indexed) */
  private int to;

  public VacationRange(int from, int to) {
    this.from = from;
    this.to   = to;
  }

  public int getFrom() { return from; }
  public int getTo()   { return to; }

  /** Cantidad de días que abarca este rango. */
  public int getDuration() { return to - from + 1; }

  /** true si el rango es de un solo día. */
  public boolean isSingleDay() { return from == to; }

  @Override
  public String toString() {
    return isSingleDay()
        ? "VC día " + from
        : "VC del " + from + " al " + to + " (" + getDuration() + " días)";
  }
}
