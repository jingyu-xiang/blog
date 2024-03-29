package com.jxiang.blog.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {

  private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  /**
   * create a jwt based on SECRET_KEY and provided userId
   *
   * @param userId sysUser's id
   * @return jwt token string
   */
  public static String createToken(final Long userId) {
    final Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);

    return Jwts.builder()
        .signWith(JwtUtils.KEY)
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 24L * 60 * 60 * 1000))
        .compact();
  }

  /**
   * check if a jwt token is valid
   *
   * @param token jwt token string
   * @return {userId=1xx2L}
   */
  public static Map<String, Object> checkToken(final String token) {
    try {
      final Jws<Claims> claimsJws = Jwts
          .parserBuilder()
          .setSigningKey(JwtUtils.KEY)
          .build()
          .parseClaimsJws(token);
      return claimsJws.getBody();
    } catch (final Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Remove "Bearer " from client-side token
   *
   * @param token jwt token
   * @return jwt token with "Bearer " removed
   */
  public static String removeHeader(final String token) {
    return token.substring(7);
  }

}
