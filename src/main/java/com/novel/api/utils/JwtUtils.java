package com.novel.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


public class JwtUtils {

    public static boolean validate(String token, String email, String secretKey) {

        String emailByToken = getEmail(token, secretKey);
        return emailByToken.equals(email) && !isTokenExpired(token, secretKey);
    }

    private static boolean isTokenExpired(String token, String secretKey) {
        Date expiration = extractAllClaims(token, secretKey).getExpiration();
        return expiration.before(new Date());
    }

    public static String generateAccessToken(String email, String secretKey, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


    public static String getEmail(String token, String secretKey) {
        return extractAllClaims(token, secretKey).get("email", String.class);
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
