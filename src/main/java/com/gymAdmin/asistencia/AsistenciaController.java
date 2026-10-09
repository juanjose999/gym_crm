package com.gymAdmin.asistencia;

import com.gymAdmin.asistencia.dto.AsistenciaRequest;
import com.gymAdmin.asistencia.dto.AsistenciaResponse;
import com.gymAdmin.common.api.ApiResponse;
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
@RequestMapping("/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AsistenciaResponse> registrar(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                     @Valid @RequestBody AsistenciaRequest request) {
        return ApiResponse.success(asistenciaService.registrar(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<AsistenciaResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(asistenciaService.listar(admin.gimnasioId()));
    }
}
