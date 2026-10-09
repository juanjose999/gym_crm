package com.gymAdmin.orden.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponse(
        Long id,
        Long socioId,
        String socioNombre,
        LocalDateTime fecha,
        BigDecimal total,
        List<ItemResponse> items
) {
    public record ItemResponse(
            Long id,
            Long productoId,
            String productoNombre,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {
    }
}
