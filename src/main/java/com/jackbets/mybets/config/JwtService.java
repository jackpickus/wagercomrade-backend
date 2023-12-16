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
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "09f329e636721b5d881eb32e0cdbd1413e76e900e60903a3c0bc459569c3200d";

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
        .signWith(getSignInKey(), SIG.HS256)
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
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
