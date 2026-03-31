package com.prd.seccontrol.extra;

public class ExcelValidationException extends RuntimeException{
  private final String position;

  private final String description;

  public ExcelValidationException(String position, String description) {
    super(description);
    this.position    = position;
    this.description = description;
  }

  public String getPosition()    { return position; }
  public String getDescription() { return description; }
}
