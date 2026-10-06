package com.gymAdmin.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record MembresiaRequestDto(
        Integer usuarioId,
        Long planId,
        Double monto,
        @JsonProperty("metodo de pago") String metodoDePago,
        @JsonProperty("fecha inicio") LocalDate fechaInicio,
        @JsonProperty("fecha fin") LocalDate fechaFin

) {
}
