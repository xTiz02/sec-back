package com.prd.seccontrol.config.security;

import com.prd.seccontrol.model.types.PermissionType;
import com.prd.seccontrol.repository.EndpointRepository;
import com.prd.seccontrol.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizationManager implements
    AuthorizationManager<RequestAuthorizationContext> {

  private final Logger log = LoggerFactory.getLogger(CustomAuthorizationManager.class);

  @Autowired
  private EndpointRepository endpointRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public @Nullable AuthorizationResult authorize(
      Supplier<? extends @Nullable Authentication> authentication,
      RequestAuthorizationContext requestContext) {

    HttpServletRequest request = requestContext.getRequest();

    String url = extractUrl(request);
    String httpMethod = request.getMethod();

    Authentication auth = authentication.get();
    if (!(auth instanceof UsernamePasswordAuthenticationToken)) {
      throw new AuthenticationCredentialsNotFoundException("Not authenticated");
    }

    boolean granted = isGranted(auth, url, httpMethod);
    log.info("Authorization decision for user: {} on URL: {} with method: {} is: {}",
        auth.getName(), url, httpMethod, granted);
    return new AuthorizationDecision(granted);
  }

  private boolean isGranted(Authentication auth, String url, String method) {

    List<GrantedAuthority> authorities = (List<GrantedAuthority>) auth.getAuthorities();
    return authorities.stream()
        .anyMatch(endpoint -> {
          String[] permission = endpoint.getAuthority().split("!:");
          return matches(permission, url, method);
        });
  }


  private String extractUrl(HttpServletRequest request) {

    String contextPath = request.getContextPath();
    String url = request.getRequestURI();
    url = url.replace(contextPath, "");

    return url;
  }

  private boolean matches(String[] permission, String url, String method) {
    Pattern pattern = Pattern.compile(permission[0]);
    Matcher matcher = pattern.matcher(url);
    if (".*".equals(permission[0])) {
      return true;
    }
    PermissionType permissionType = PermissionType.valueOf(permission[1]);
    return matcher.matches()
        && permissionType.equals(mapHttpMethodToPermissionType(method));
  }

  private PermissionType mapHttpMethodToPermissionType(String httpMethod) {
    return switch (httpMethod.toUpperCase()) {
      case "GET" -> PermissionType.READ;
      case "POST", "PUT", "PATCH", "DELETE" -> PermissionType.WRITE;
      default -> PermissionType.NONE;
    };
  }


}
