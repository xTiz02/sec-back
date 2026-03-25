package com.prd.seccontrol.extra;

import java.util.Map;

public class GenericException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private Map<String, Object> properties;

  public GenericException() {
    super();
  }

  public GenericException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public GenericException(final String message) {
    super(message);
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

}
