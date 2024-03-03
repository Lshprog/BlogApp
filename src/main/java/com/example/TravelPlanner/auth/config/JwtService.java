package com.example.TravelPlanner.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt_secret}")
    String jwtSecret;

    private final RedisTemplate<String, String> redisTemplate;

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSecretSigningKey())
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSecretSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSecretSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private long calculateDurationUntilExpiration(Date expiration) {
        long difference = expiration.getTime() - System.currentTimeMillis();
        return difference > 0 ? difference / 1000 : 0; // Convert to seconds, ensure non-negative
    }


    public void blacklistToken(String token) {
        Date expirationDate = extractExpiration(token);
        long durationInSeconds = calculateDurationUntilExpiration(expirationDate);

        if (durationInSeconds > 0) {
            redisTemplate.opsForValue().set("BL_" + token, "blacklisted", durationInSeconds, TimeUnit.SECONDS);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean isBlacklisted = redisTemplate.hasKey("BL_" + token);
        return Boolean.TRUE.equals(isBlacklisted);
    }

}
