package com.prd.seccontrol.config.filters;

import com.prd.seccontrol.util.SEConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BodyCachingFilter extends OncePerRequestFilter {

  // coincide con truncateString(json, 60_000) del evento
  private static final int CACHE_LIMIT = 64 * 1024;

  private static final Set<String> SKIP_CONTAINS = Set.of("upload-file", "download-file");

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    if (shouldSkip(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    String contentType = request.getContentType();
    if (contentType != null && contentType.contains("application/json")) {

      ContentCachingRequestWrapper wrappedRequest =
          new ContentCachingRequestWrapper(request, CACHE_LIMIT) { // límite obligatorio
            @Override
            protected void handleContentOverflow(int contentCacheLimit) {
              logger.warn(String.format("Request body truncated at {} bytes for uri={}",
                  String.valueOf(contentCacheLimit), request.getRequestURI()));
            }
          };

      filterChain.doFilter(wrappedRequest, response); // controller lee aquí

      // Después del chain el buffer ya tiene el body
      String body = wrappedRequest.getContentAsString();
      wrappedRequest.setAttribute(SEConstants.INTERCEPTED_JSON, body);

    } else {
      filterChain.doFilter(request, response);
    }
  }

  private boolean shouldSkip(String uri) {
    return SKIP_CONTAINS.stream().anyMatch(uri::contains);
  }
}
