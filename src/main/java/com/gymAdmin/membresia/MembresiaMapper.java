package com.gymAdmin.membresia;

import com.gymAdmin.membresia.dto.MembresiaResponse;
import com.gymAdmin.membresia.dto.PagoMembresiaResponse;
import com.gymAdmin.plan.Plan;
import com.gymAdmin.usuario.Usuario;

public final class MembresiaMapper {

    private MembresiaMapper() {
    }

    public static MembresiaResponse toResponse(Membresia membresia) {
        Usuario usuario = membresia.getUsuario();
        Plan plan = membresia.getPlan();

        return new MembresiaResponse(
                membresia.getId(),
                new MembresiaResponse.UsuarioResumen(usuario.getId(), usuario.getNombreCompleto(), usuario.getEmail()),
                new MembresiaResponse.PlanResumen(plan.getId(), plan.getNombre(), plan.getPrecio()),
                membresia.getFechaInicio(),
                membresia.getFechaFin(),
                membresia.getEstado(),
                membresia.getTotalPagado(),
                membresia.getSaldoPendiente(),
                membresia.getPagos().stream().map(MembresiaMapper::toPagoResponse).toList()
        );
    }

    private static PagoMembresiaResponse toPagoResponse(PagoMembresia pago) {
        return new PagoMembresiaResponse(
                pago.getId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getCreatedAt()
        );
    }
}
