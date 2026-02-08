package com.sv.meeting.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key;
    private final String issuer;
    private final long expirySeconds;


    public JwtUtil(@Value("${app.security.jwt.secret}") String secret, @Value("${app.security.jwt.issuer}") String issuer,
                   @Value("${app.security.jwt.expirySeconds}") long expirySeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expirySeconds = expirySeconds;
    }

    public String generateToken(long userId, String email, String rolesCsv) {
        Instant now = Instant.now();
        return Jwts.builder().setIssuer(issuer).setSubject(String.valueOf(userId)).setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirySeconds))).addClaims(Map.of("email", email, "roles", rolesCsv))
                .signWith(key, SignatureAlgorithm.HS256) // TODO EXPLORE ON different Algorithms
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).requireIssuer(issuer).build().parseClaimsJws(token);
    }
}
