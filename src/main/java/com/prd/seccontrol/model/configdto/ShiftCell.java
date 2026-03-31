package com.prd.seccontrol.model.configdto;

public class ShiftCell {

  private int day;

  private String shift;

  private int excelRow;

  private int excelCol;

  private String excelColLetter;

  public ShiftCell() {
  }

  public ShiftCell(int day, String shift, int excelRow, int excelCol, String excelColLetter) {
    this.day = day;
    this.shift = shift;
    this.excelRow = excelRow;
    this.excelCol = excelCol;
    this.excelColLetter = excelColLetter;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int v) {
    this.day = v;
  }

  public String getShift() {
    return shift;
  }

  public void setShift(String v) {
    this.shift = v;
  }

  public int getExcelRow() {
    return excelRow;
  }

  public void setExcelRow(int v) {
    this.excelRow = v;
  }

  public int getExcelCol() {
    return excelCol;
  }

  public void setExcelCol(int v) {
    this.excelCol = v;
  }

  public String getExcelColLetter() {
    return excelColLetter;
  }

  public void setExcelColLetter(String v) {
    this.excelColLetter = v;
  }

  /**
   * Referencia estilo Excel: "C7" (col C, fila 7)
   */
  public String getCellRef() {
    return excelColLetter + excelRow;
  }

  @Override
  public String toString() {
    return "ShiftCell{day=" + day + ", shift='" + shift + "', ref=" + getCellRef() + '}';
  }
}
