package com.example.citylibrary.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    private static final String SECRET_KEY = "myverySecretKey";

    public String extractUsername(String jwtToken) {
        return null;
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(jwtToken).getPayload();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
