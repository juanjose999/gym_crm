package com.gymAdmin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Configuración de JWT leída desde application.properties (prefijo {@code jwt}).
 *
 * @param secret                 clave HMAC en Base64 (mínimo 256 bits)
 * @param accessTokenExpiration  vigencia del token de acceso
 * @param refreshTokenExpiration vigencia del token de refresco
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Duration accessTokenExpiration,
        Duration refreshTokenExpiration
) {
}
