package com.gymAdmin.asistencia;

import com.gymAdmin.asistencia.dto.AsistenciaResponse;
import com.gymAdmin.usuario.Usuario;

public final class AsistenciaMapper {

    private AsistenciaMapper() {
    }

    public static AsistenciaResponse toResponse(Asistencia asistencia) {
        Usuario usuario = asistencia.getUsuario();
        return new AsistenciaResponse(
                asistencia.getId(),
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                asistencia.getFechaEntrada(),
                asistencia.getTipoAcceso()
        );
    }
}
