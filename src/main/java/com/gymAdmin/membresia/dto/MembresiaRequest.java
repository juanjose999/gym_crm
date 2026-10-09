package com.gymAdmin.membresia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @param fechaFin   opcional; si no se envía se calcula con la duración del plan
 * @param monto      pago inicial opcional
 * @param metodoPago obligatorio si se envía un monto mayor a 0
 */
public record MembresiaRequest(

        @NotNull(message = "El socio es obligatorio")
        Long socioId,

        @NotNull(message = "El plan es obligatorio")
        Long planId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate fechaFin,

        @PositiveOrZero(message = "El monto no puede ser negativo")
        BigDecimal monto,

        String metodoPago
) {
}
