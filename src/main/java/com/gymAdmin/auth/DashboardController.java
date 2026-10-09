package com.gymAdmin.auth;

import com.gymAdmin.auth.dto.DashboardResponse;
import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /** Mismo resumen del gimnasio que se entrega en signup/login, para refrescarlo sin volver a autenticarse. */
    @GetMapping
    public ApiResponse<DashboardResponse> obtener(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(dashboardService.construir(admin.gimnasioId()));
    }
}
