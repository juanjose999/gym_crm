package com.gymAdmin.auth;

import com.gymAdmin.auth.dto.AccesoSocioRequest;
import com.gymAdmin.auth.dto.AuthResponse;
import com.gymAdmin.auth.dto.LoginRequest;
import com.gymAdmin.auth.dto.RefreshTokenRequest;
import com.gymAdmin.gimnasio.Gimnasio;
import com.gymAdmin.security.JwtService;
import com.gymAdmin.security.TokenType;
import com.gymAdmin.usuario.Usuario;
import com.gymAdmin.usuario.UsuarioMapper;
import com.gymAdmin.usuario.UsuarioService;
import com.gymAdmin.usuario.dto.RegistroAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse registrarAdmin(RegistroAdminRequest request) {
        return construirRespuesta(usuarioService.registrarAdmin(request));
    }

    /**
     * Login con contraseña; solo los administradores tienen una, así que un socio recibe 401.
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        return construirRespuesta(usuarioService.obtenerPorEmail(request.email()));
    }

    /**
     * Acceso de socios solo con el correo. El token resultante tiene rol SOCIO
     * y únicamente permite consultar /mi-cuenta.
     */
    public AuthResponse accesoSocio(AccesoSocioRequest request) {
        return construirRespuesta(usuarioService.obtenerSocioPorEmail(request.email()));
    }

    public AuthResponse refrescar(RefreshTokenRequest request) {
        String email = jwtService.extractSubject(request.refreshToken(), TokenType.REFRESH);
        return construirRespuesta(usuarioService.obtenerPorEmail(email));
    }

    private AuthResponse construirRespuesta(Usuario usuario) {
        Gimnasio gimnasio = usuario.getGimnasio();
        return new AuthResponse(
                new AuthResponse.Tokens(
                        jwtService.generateAccessToken(usuario.getEmail()),
                        jwtService.generateRefreshToken(usuario.getEmail())
                ),
                UsuarioMapper.toResponse(usuario),
                new AuthResponse.GimnasioResumen(gimnasio.getId(), gimnasio.getNombre())
        );
    }
}
