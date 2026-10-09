package com.gymAdmin.plan.dto;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer duracionDias,
        BigDecimal precio
) {
}
