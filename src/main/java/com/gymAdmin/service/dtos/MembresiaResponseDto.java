package com.gymAdmin.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gymAdmin.entity.PagoMembresia;

import java.time.LocalDate;
import java.util.List;

public record MembresiaResponseDto(
        Long id,
        UsuarioSummaryDto usuario,
        PlanSummaryDto plan,
        List<PagoMembresiaDto> pagosMembresia,
        LocalDate fechaInicio,
        LocalDate fechaFin
        // Opcional: List<PagoResponseDto> pagos (depende si necesitas mostrarlos aquí)
) {
    // DTOs internos resumidos para evitar bucles y sobrecargar la respuesta
    public record UsuarioSummaryDto(
            Long id,
            String nombre,
            String email
    ) {}

    public record PlanSummaryDto(
            Long id,
            String nombre,
            Double precio
    ) {}

    public record PagoMembresiaDto(
            Long id,
            Double monto,
            @JsonProperty("metodo de pago") String metodoDePago
    ){

    }
}