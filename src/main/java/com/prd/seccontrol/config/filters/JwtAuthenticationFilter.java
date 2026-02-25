package com.prd.seccontrol.config.filters;

import com.prd.seccontrol.service.impl.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = null;

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("access_token".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    //get form header if not found in cookie
    if (token == null) {
      token = extractToken(request);
    }

    if (token != null && jwtService.validate(token)) {

      String username = jwtService.getSubject(token);
      Claims claims = jwtService.getClaims(token);

      List<SimpleGrantedAuthority> authorities =
          ((List<?>) claims.get("permissions"))
              .stream()
              .map(obj -> (Map<?, ?>) obj)
              .map(map -> map.get("authority").toString())
              .map(SimpleGrantedAuthority::new)
              .toList();

      Authentication auth =
          new UsernamePasswordAuthenticationToken(
              username,
              null,
              authorities
          );

      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      return header.substring(7);
    }
    return null;
  }
}
