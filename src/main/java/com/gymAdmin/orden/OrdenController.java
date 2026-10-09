package com.gymAdmin.orden;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.orden.dto.OrdenRequest;
import com.gymAdmin.orden.dto.OrdenResponse;
import com.gymAdmin.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrdenResponse> crear(@AuthenticationPrincipal UsuarioAutenticado admin,
                                            @Valid @RequestBody OrdenRequest request) {
        return ApiResponse.success(ordenService.crear(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<OrdenResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(ordenService.listar(admin.gimnasioId()));
    }
}
