package com.gymAdmin.orden.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrdenRequest(

        @NotNull(message = "El socio es obligatorio")
        Long socioId,

        @NotEmpty(message = "La orden debe tener al menos un ítem")
        List<@Valid ItemRequest> items
) {
    public record ItemRequest(

            @NotNull(message = "El producto es obligatorio")
            Long productoId,

            @NotNull(message = "La cantidad es obligatoria")
            @Positive(message = "La cantidad debe ser mayor a 0")
            Integer cantidad
    ) {
    }
}
