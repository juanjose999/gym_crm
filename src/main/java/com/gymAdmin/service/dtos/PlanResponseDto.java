package com.gymAdmin.service.dtos;

public record PlanResponseDto(
        Long id,
        String nombre,
        String descripcion,
        Integer duracionDias,
        Double precio
) {
}
