package com.gymAdmin.membresia.dto;

import com.gymAdmin.membresia.EstadoMembresia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record MembresiaResponse(
        Long id,
        UsuarioResumen usuario,
        PlanResumen plan,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoMembresia estado,
        BigDecimal totalPagado,
        BigDecimal saldoPendiente,
        List<PagoMembresiaResponse> pagos
) {
    public record UsuarioResumen(Long id, String nombre, String email) {
    }

    public record PlanResumen(Long id, String nombre, BigDecimal precio) {
    }
}
