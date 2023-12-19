package com.jackbets.mybets.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.jackbets.mybets.auth.ApplicationUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllCliams(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(ApplicationUser applicationUser) {
        return generateToken(new HashMap<>(), applicationUser);
    }

    public String generateToken(Map<String, Object> extraClaims, ApplicationUser applicationUser) {
        return Jwts.builder()
        .claims(extraClaims)
        .subject(applicationUser.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
        .signWith(this.secretKey)
        .compact();
    }

    public boolean isTokenValid(String token, ApplicationUser applicationUser) {
        final String username = extractUsername(token);
        return (username.equals(applicationUser.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllCliams(String token) {
        return Jwts
            .parser()
            .verifyWith(this.secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

}
