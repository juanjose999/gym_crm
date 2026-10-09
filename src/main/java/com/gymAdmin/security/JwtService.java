package com.gymAdmin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private static final String CLAIM_TYPE = "type";

    private final JwtProperties properties;
    private final Key signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
    }

    public String generateAccessToken(String email) {
        return generateToken(email, TokenType.ACCESS, properties.accessTokenExpiration());
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, TokenType.REFRESH, properties.refreshTokenExpiration());
    }

    /**
     * Valida firma, expiración y tipo del token y devuelve el email del usuario.
     *
     * @throws io.jsonwebtoken.JwtException si el token no es válido
     */
    public String extractSubject(String token, TokenType expectedType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!expectedType.name().equals(claims.get(CLAIM_TYPE, String.class))) {
            throw new MalformedJwtException("Tipo de token inválido");
        }
        return claims.getSubject();
    }

    private String generateToken(String email, TokenType type, Duration expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .claim(CLAIM_TYPE, type.name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiration)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
