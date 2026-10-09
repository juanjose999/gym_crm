package com.gymAdmin.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Registro público: crea un gimnasio nuevo y su administrador.
 */
public record RegistroAdminRequest(

        @NotBlank(message = "El nombre del gimnasio es obligatorio")
        @Size(max = 100, message = "El nombre del gimnasio no puede superar los 100 caracteres")
        String nombreGimnasio,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
        String apellidos,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El formato del teléfono no es válido")
        String telefono,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password
) {
}
