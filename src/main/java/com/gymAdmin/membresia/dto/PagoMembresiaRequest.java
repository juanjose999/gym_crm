package com.gymAdmin.membresia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PagoMembresiaRequest(

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        @NotBlank(message = "El método de pago es obligatorio")
        @Size(max = 50, message = "El método de pago no puede superar los 50 caracteres")
        String metodoPago
) {
}
