package com.prd.seccontrol.config.filters.event;

import com.prd.seccontrol.model.configdto.RequestCompletedEvent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
  private static final Set<String> SKIP_URIS = Set.of("/health", "/v1/event");
  private static final Set<String> SKIP_CONTAINS = Set.of("upload-file", "download-file");
  private static final int CACHE_LIMIT = 64 * 1024;
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    long startTime = System.currentTimeMillis();
    String uri = request.getRequestURI();
    ContentCachingRequestWrapper wrappedRequest = null;
    if (!shouldSkip(uri)) {
      wrappedRequest = new ContentCachingRequestWrapper(request, CACHE_LIMIT);
    }

    try {
      filterChain.doFilter(wrappedRequest != null ? wrappedRequest : request, response);
    } finally {
      long duration = System.currentTimeMillis() - startTime;

      logger.info("method={}, path={}, status={}, duration_ms={}",
          request.getMethod(), uri, response.getStatus(), duration);

      String body = wrappedRequest != null && !Objects.equals(request.getMethod(), "GET")
          ? wrappedRequest.getContentAsString() : null;

      // Solo publica → no bloquea el thread
      eventPublisher.publishEvent(new RequestCompletedEvent(
          this,
          request.getMethod(),
          uri,
          request.getRequestURL().toString(),
          response.getStatus(),
          duration,
          startTime,
          request.getParameterMap().toString(),
          body,
          SecurityContextHolder.getContext().getAuthentication()
      ));
    }
  }

  private boolean shouldSkip(String uri) {
    if (SKIP_URIS.contains(uri)) {
      return true;
    }
    return SKIP_CONTAINS.stream().anyMatch(uri::contains);
  }
}
