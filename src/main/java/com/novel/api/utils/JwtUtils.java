package com.novel.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {


    @Value("${jwt.token.expired-time-ms}")
    private Long expiredMs;

    public static boolean validate(String token, String name, String secretKey) {

        String usernameByToken = getUsername(token, secretKey);
        return usernameByToken.equals(name) && !isTokenExpired(token, secretKey);
    }

    private static boolean isTokenExpired(String token, String secretKey) {
        Date expiration = extractAllClaims(token, secretKey).getExpiration();
        return expiration.before(new Date());
    }


    public String createJwt(String username, String role, String secretKey) {
        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


    public static String getUsername(String token, String secretKey) {
        return extractAllClaims(token, secretKey).get("username", String.class);
    }

    private static Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSigningKey(String secretKey) {

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
