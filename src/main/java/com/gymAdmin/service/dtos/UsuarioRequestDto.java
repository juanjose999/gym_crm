package com.gymAdmin.service.dtos;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UsuarioRequestDto(

        @NotBlank(message = "El nombre es obligatorios")
        @Size(max = 100, message = "El nombre no pueden superar los 100 caracteres")
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
) {}