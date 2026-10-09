package com.gymAdmin.membresia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoMembresiaResponse(
        Long id,
        BigDecimal monto,
        String metodoPago,
        LocalDateTime fechaPago
) {
}
