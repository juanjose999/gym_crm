package com.gymAdmin.membresia.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Los pagos no se modifican aquí: se gestionan en /membresias/{id}/pagos.
 *
 * @param fechaFin opcional; si no se envía se calcula con la duración del plan
 */
public record MembresiaUpdateRequest(

        @NotNull(message = "El plan es obligatorio")
        Long planId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate fechaFin
) {
}
