package com.jxiang.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "u8dh39hd9312";

    /**
     * create a jwt based on SECRET_KEY and provided userId
     *
     * @param userId sysUser's id
     * @return jwt token string
     */
    public static String createToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        JwtBuilder jwtBuilder = Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000)); // valid for 2 months

        return jwtBuilder.compact();
    }

    /**
     * check if a jwt token is valid
     *
     * @param token jwt token string
     * @return {userId=1xx2L}
     */
    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt parse = Jwts.parser().setSigningKey(SECRET_KEY).parse(token);
            return (Map<String, Object>) parse.getBody(); // -> {userId=1xx2L}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
