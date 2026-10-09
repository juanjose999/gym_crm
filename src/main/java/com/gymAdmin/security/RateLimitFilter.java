package com.gymAdmin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Limita las peticiones con un token bucket en memoria.
 * <ul>
 *     <li>/auth/**: por IP, con un límite estricto para frenar fuerza bruta.</li>
 *     <li>Resto: por usuario autenticado (o por IP si no hay token válido).</li>
 * </ul>
 * Va después de {@link JwtAuthenticationFilter} para conocer al usuario. No se registra como bean
 * para que Spring Boot no lo agregue también a la cadena del servlet y cuente cada petición dos veces.
 * <p>
 * El estado vive en la instancia: con varias réplicas cada una aplica su propio límite.
 */
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String PREFIJO_AUTH = "/auth/";
    /** Sobre este tamaño se eliminan los buckets inactivos para que el mapa no crezca sin límite. */
    private static final int MAX_BUCKETS = 10_000;

    private final RateLimitProperties properties;
    private final ConcurrentMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public RateLimitFilter(RateLimitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !properties.enabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean esAuth = request.getRequestURI().startsWith(PREFIJO_AUTH);
        RateLimitProperties.Limite limite = esAuth ? properties.auth() : properties.api();
        String clave = esAuth ? "auth:ip:" + request.getRemoteAddr() : "api:" + identificar(request);

        if (buckets.size() > MAX_BUCKETS) {
            limpiarInactivos();
        }
        TokenBucket bucket = buckets.computeIfAbsent(clave, k -> new TokenBucket(limite));

        long esperaNanos = bucket.consumir();
        response.setHeader("X-RateLimit-Limit", String.valueOf(limite.capacity()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(bucket.disponibles()));

        if (esperaNanos > 0) {
            long segundos = Math.max(1, (esperaNanos + 999_999_999L) / 1_000_000_000L);
            log.warn("Rate limit excedido para {} en {}", clave, request.getRequestURI());
            response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(segundos));
            SecurityConfig.writeError(response, HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiadas solicitudes, intenta nuevamente en " + segundos + " segundos");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static String identificar(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails usuario) {
            return "user:" + usuario.getUsername();
        }
        return "ip:" + request.getRemoteAddr();
    }

    private void limpiarInactivos() {
        long ahora = System.nanoTime();
        buckets.values().removeIf(b -> b.inactivo(ahora));
    }

    /** Bucket que se recarga de forma continua: {@code capacity} fichas cada {@code period}. */
    private static final class TokenBucket {

        private final long capacidad;
        private final long nanosPorFicha;
        private double fichas;
        private long ultimaRecarga;

        TokenBucket(RateLimitProperties.Limite limite) {
            this.capacidad = limite.capacity();
            this.nanosPorFicha = limite.period().toNanos() / limite.capacity();
            this.fichas = capacidad;
            this.ultimaRecarga = System.nanoTime();
        }

        /** Consume una ficha; devuelve 0 si se permitió o los nanosegundos a esperar si no. */
        synchronized long consumir() {
            recargar(System.nanoTime());
            if (fichas >= 1) {
                fichas -= 1;
                return 0;
            }
            return (long) ((1 - fichas) * nanosPorFicha);
        }

        synchronized long disponibles() {
            return (long) fichas;
        }

        /** Un bucket lleno equivale a uno nuevo, así que se puede descartar sin perder información. */
        synchronized boolean inactivo(long ahora) {
            recargar(ahora);
            return fichas >= capacidad;
        }

        private void recargar(long ahora) {
            fichas = Math.min(capacidad, fichas + (double) (ahora - ultimaRecarga) / nanosPorFicha);
            ultimaRecarga = ahora;
        }
    }
}
