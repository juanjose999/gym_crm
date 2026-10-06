package com.gymAdmin.service.mappers;

import com.gymAdmin.entity.Plan;
import com.gymAdmin.service.dtos.PlanRequestDto;
import com.gymAdmin.service.dtos.PlanResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    public static Plan toEntity(PlanRequestDto planRequestDto) {
        Plan plan = new Plan();
        plan.setNombre(planRequestDto.nombre());
        plan.setDuracion_dias(planRequestDto.duracionDias());
        plan.setDescripcion(planRequestDto.descripcion());
        plan.setPrecio(planRequestDto.precio());
        return plan;
    }

    public static PlanResponseDto toPlanResponsetDto(Plan plan) {
        PlanResponseDto responseDto = new PlanResponseDto(
                plan.getId(),
                plan.getNombre(),
                plan.getDescripcion(),
                plan.getDuracion_dias(),
                plan.getPrecio()
        );
        return responseDto;
    }

}
