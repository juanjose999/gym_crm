package com.gymAdmin.service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlanRequestDto(
        String nombre,
        String descripcion,
        @JsonProperty("duracion dias")  Integer duracionDias,
        Double precio
) {
}
