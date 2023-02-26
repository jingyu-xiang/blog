package com.jxiang.blog.util.beans;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

  final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  /**
   * create a jwt based on SECRET_KEY and provided userId
   *
   * @param userId sysUser's id
   * @return jwt token string
   */
  public String createToken(Long userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);

    return Jwts.builder()
        .signWith(KEY)
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
  public Map<String, Object> checkToken(String token) {
    try {
      final Jws<Claims> claimsJws = Jwts
          .parserBuilder()
          .setSigningKey(KEY)
          .build()
          .parseClaimsJws(token);
      return claimsJws.getBody();
    } catch (Exception e) {
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
  public String removeHeader(String token) {
    return token.substring(7);
  }

}
