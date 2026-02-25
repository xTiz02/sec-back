package com.prd.seccontrol.util;

public class SEConstants {
  public static final String API_VERSION_V1 = "/v1";
  public static final String API_VERSION_V2 = "/v2";

  public static final String SECURE_ENDPOINT_PREFIX = "/secure/api";

  public static final String SECURE_BASE_ENDPOINT = SECURE_ENDPOINT_PREFIX + API_VERSION_V1;
  public static final String SECURE_BASE_ENDPOINT_V2 = SECURE_ENDPOINT_PREFIX + API_VERSION_V2;

  public static final String PUBLIC_BASE_ENDPOINT = "/public/api" + API_VERSION_V1;

  public static final String DATE_FORMAT = "dd/MM/yyyy";

  public static final String PERU_LOCAL_TIMEZONE = "-05:00";
}
