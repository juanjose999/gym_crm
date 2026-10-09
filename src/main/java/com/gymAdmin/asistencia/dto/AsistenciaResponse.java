package com.gymAdmin.asistencia.dto;

import java.time.LocalDateTime;

public record AsistenciaResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        String usuarioEmail,
        LocalDateTime fechaEntrada,
        String tipoAcceso
) {
}
