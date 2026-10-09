package com.gymAdmin.usuario;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.security.UsuarioAutenticado;
import com.gymAdmin.usuario.dto.SocioRequest;
import com.gymAdmin.usuario.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UsuarioResponse> crear(@AuthenticationPrincipal UsuarioAutenticado admin,
                                              @Valid @RequestBody SocioRequest request) {
        return ApiResponse.success(socioService.crear(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<UsuarioResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(socioService.listar(admin.gimnasioId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<UsuarioResponse> buscarPorId(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                    @PathVariable Long id) {
        return ApiResponse.success(socioService.buscarPorId(admin.gimnasioId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UsuarioResponse> actualizar(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody SocioRequest request) {
        return ApiResponse.success(socioService.actualizar(admin.gimnasioId(), id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@AuthenticationPrincipal UsuarioAutenticado admin, @PathVariable Long id) {
        socioService.eliminar(admin.gimnasioId(), id);
    }
}
