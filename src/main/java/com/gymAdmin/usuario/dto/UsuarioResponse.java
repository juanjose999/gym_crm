package com.gymAdmin.usuario.dto;

import com.gymAdmin.usuario.Rol;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombres,
        String apellidos,
        String telefono,
        String email,
        Rol rol,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
