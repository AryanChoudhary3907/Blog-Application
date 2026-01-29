package com.Aryan.Blog.Application.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    // Access Token = 10 minutes
    private final long ACCESS_TOKEN_EXPIRY = 10 * 60 * 1000;

    // Refresh Token = 7 days
    private final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000;

    // Get Secret Key
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Generate Access Token
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", roles)
                .claim("tokenType", "Access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(getSecretKey())
                .compact();
    }

    // Generate Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", roles)
                .claim("tokenType", "Refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(getSecretKey())
                .compact();
    }

    // Extract Username
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extract token type (Access / Refresh)
    public String getTokenType(String token) {
        return getClaims(token).get("tokenType", String.class);
    }

    // Validate token only (no need UserDetails)
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Internal: Get Claims
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract roles
    public List<String> getRoles(String token) {
        return getClaims(token).get("role", List.class);
    }
}
