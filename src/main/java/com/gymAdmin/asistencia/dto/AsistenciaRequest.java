package com.gymAdmin.asistencia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * @param fechaEntrada opcional; si no se envía se usa la fecha y hora actual
 */
public record AsistenciaRequest(

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        String email,

        LocalDateTime fechaEntrada,

        @NotBlank(message = "El tipo de acceso es obligatorio")
        @Size(max = 50, message = "El tipo de acceso no puede superar los 50 caracteres")
        String tipoAcceso
) {
}
