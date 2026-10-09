package com.gymAdmin.micuenta;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.membresia.MembresiaService;
import com.gymAdmin.membresia.dto.MembresiaResponse;
import com.gymAdmin.orden.OrdenService;
import com.gymAdmin.orden.dto.OrdenResponse;
import com.gymAdmin.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Consultas del socio sobre su propia información. Solo accesible con rol SOCIO;
 * el id se toma siempre del token, nunca de la petición.
 */
@RestController
@RequestMapping("/mi-cuenta")
@RequiredArgsConstructor
public class MiCuentaController {

    private final MembresiaService membresiaService;
    private final OrdenService ordenService;

    @GetMapping("/membresias")
    public ApiResponse<List<MembresiaResponse>> misMembresias(@AuthenticationPrincipal UsuarioAutenticado socio) {
        return ApiResponse.success(membresiaService.listarPorSocio(socio.id()));
    }

    @GetMapping("/compras")
    public ApiResponse<List<OrdenResponse>> misCompras(@AuthenticationPrincipal UsuarioAutenticado socio) {
        return ApiResponse.success(ordenService.listarPorSocio(socio.id()));
    }
}
