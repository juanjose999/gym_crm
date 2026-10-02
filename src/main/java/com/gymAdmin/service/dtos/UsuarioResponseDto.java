package com.gymAdmin.service.dtos;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UsuarioResponseDto(
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
        String email

) {

}
