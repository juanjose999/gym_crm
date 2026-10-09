package com.gymAdmin.plan;

import com.gymAdmin.plan.dto.PlanRequest;
import com.gymAdmin.plan.dto.PlanResponse;

public final class PlanMapper {

    private PlanMapper() {
    }

    public static Plan toEntity(PlanRequest request) {
        Plan plan = new Plan();
        actualizar(plan, request);
        return plan;
    }

    public static void actualizar(Plan plan, PlanRequest request) {
        plan.setNombre(request.nombre());
        plan.setDescripcion(request.descripcion());
        plan.setDuracionDias(request.duracionDias());
        plan.setPrecio(request.precio());
    }

    public static PlanResponse toResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getNombre(),
                plan.getDescripcion(),
                plan.getDuracionDias(),
                plan.getPrecio()
        );
    }
}
