package com.nucleusteq.expensetrackerbackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {

    @Value("${jwt.secret}") // Secret key application.properties se lega
    private String secretKey;
    

    // Get Signing Key (256-bit Secret Key)
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiry
                .signWith(getSigningKey())
                .compact();
    }

    // Extract Claims from Token
    public Claims extractClaims(String token) {
        return Jwts.parser() // Use `parser()` instead of `parserBuilder()`
        .verifyWith(getSigningKey()) // Use `verifyWith()` instead of `setSigningKey()`
        .build()
        .parseSignedClaims(token) // Use `parseSignedClaims()` instead of `parseClaimsJws()`
        .getPayload(); // Use `getPayload()` instead of `getBody()`
    }
    // Validate Token
    public boolean isTokenValid(String token, String userEmail) {
        final String extractedEmail = extractClaims(token).getSubject();
        return (extractedEmail.equals(userEmail) && !isTokenExpired(token));
    }

    // Check Token Expiry
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}


