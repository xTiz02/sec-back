package com.prd.seccontrol.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  private static final Logger log = LoggerFactory.getLogger(JwtService.class);

  @Value("${security.jwt.expiration-in-minutes}")
  private long expiration;

  @Value("${security.jwt.secret-key}")
  private String secret;


  public String generateToken(UserDetails userDetails) {

    Date issuedAt = new Date(System.currentTimeMillis());
    Date expiration = new Date((issuedAt.getTime() + this.expiration)*60L*1000L);
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("permissions", userDetails.getAuthorities())
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(getKey(secret))
        .compact();
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getSubject(String token) {
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Date getIssuedAt(String token) {
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getIssuedAt();
  }

  public String extractJti(String token) {
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getId();
  }

  public boolean validate(String token){
    try {
      Jwts.parser()
          .verifyWith(getKey(secret))
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("token expired");
    } catch (UnsupportedJwtException e) {
      log.warn("token unsupported");
    } catch (MalformedJwtException e) {
      log.warn("token malformed");
    } catch (IllegalArgumentException e) {
      log.warn("illegal args");
    }
    return false;
  }

  private SecretKey getKey(String secret) {
    byte[] secretBytes = Decoders.BASE64URL.decode(secret);
    return Keys.hmacShaKeyFor(secretBytes);
  }
}
