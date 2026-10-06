package com.gymAdmin.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.node.StringNode;

public record PagoMembresiaRequest(
        Integer membresiaId,
        Double monto,
        @JsonProperty("metodo de pago") String metodo_pago
) {
}
