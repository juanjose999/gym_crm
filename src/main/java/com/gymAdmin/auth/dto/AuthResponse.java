package com.gymAdmin.auth.dto;

import com.gymAdmin.usuario.dto.UsuarioResponse;

public record AuthResponse(
        Tokens tokens,
        UsuarioResponse user,
        GimnasioResumen gimnasio,
        DashboardResponse dashboardResponse
) {
    public record Tokens(
            String accessToken,
            String refreshToken
    ) {
    }

    public record GimnasioResumen(
            Long id,
            String nombre
    ) {
    }
}
