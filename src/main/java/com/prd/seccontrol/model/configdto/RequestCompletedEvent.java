package com.prd.seccontrol.model.configdto;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

public class RequestCompletedEvent extends ApplicationEvent {

  private final String method;
  private final String uri;
  private final String url;
  private final int responseStatus;
  private final long duration;
  private final long startTime;
  private final String parameters;
  private final String json;
  private final Authentication authentication;

  public RequestCompletedEvent(Object source, String method, String uri,
      String url, int responseStatus, long duration,
      long startTime, String parameters,
      String json, Authentication authentication) {
    super(source);
    this.method = method;
    this.uri = uri;
    this.url = url;
    this.responseStatus = responseStatus;
    this.duration = duration;
    this.startTime = startTime;
    this.parameters = parameters;
    this.json = json;
    this.authentication = authentication;
  }

  public String getMethod() {
    return method;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public String getJson() {
    return json;
  }

  public String getParameters() {
    return parameters;
  }

  public long getStartTime() {
    return startTime;
  }

  public long getDuration() {
    return duration;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

  public String getUrl() {
    return url;
  }

  public String getUri() {
    return uri;
  }
}
