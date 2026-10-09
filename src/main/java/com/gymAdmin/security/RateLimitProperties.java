package com.gymAdmin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * Límites de peticiones leídos desde application.properties (prefijo {@code rate-limit}).
 *
 * @param enabled permite desactivar el control (p. ej. en pruebas de carga)
 * @param auth    límite por IP para /auth/** (frena fuerza bruta en login y enumeración de correos)
 * @param api     límite por usuario autenticado (o por IP si no lo está) para el resto de la API
 */
@ConfigurationProperties(prefix = "rate-limit")
public record RateLimitProperties(
        @DefaultValue("true") boolean enabled,
        @DefaultValue({"10", "1m"}) Limite auth,
        @DefaultValue({"120", "1m"}) Limite api
) {

    /**
     * @param capacity peticiones permitidas por periodo
     * @param period   ventana en la que se recargan por completo
     */
    public record Limite(int capacity, Duration period) {
    }
}
