package com.gymAdmin.auth;

import com.gymAdmin.auth.dto.AccesoSocioRequest;
import com.gymAdmin.auth.dto.AuthResponse;
import com.gymAdmin.auth.dto.LoginRequest;
import com.gymAdmin.auth.dto.RefreshTokenRequest;
import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.usuario.dto.RegistroAdminRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Registra un gimnasio nuevo junto con su administrador. */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody RegistroAdminRequest request) {
        return ApiResponse.success(authService.registrarAdmin(request));
    }

    /** Login de administradores (correo y contraseña). */
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    /** Acceso de socios solo con su correo. */
    @PostMapping("/socio/acceso")
    public ApiResponse<AuthResponse> accesoSocio(@Valid @RequestBody AccesoSocioRequest request) {
        return ApiResponse.success(authService.accesoSocio(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(authService.refrescar(request));
    }
}
