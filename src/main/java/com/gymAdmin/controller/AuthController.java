package com.gymAdmin.controller;

import com.gymAdmin.config.JwtService;
import com.gymAdmin.entity.Usuario;
import com.gymAdmin.service.UsuarioService;
import com.gymAdmin.service.dtos.LoginRequestDto;
import com.gymAdmin.service.dtos.UsuarioRequestDto;
import com.gymAdmin.service.dtos.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/singup")
    public ResponseEntity<ResponseCustom> save(@RequestBody UsuarioRequestDto requestDto) {
        UsuarioResponseDto savedUser = usuarioService.save(requestDto);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.email());
        final String token = jwtService.generateToken(userDetails);
        final String tokenRefresh = jwtService.generateRefreshToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseCustom.success(
                        Map.of(
                                "tokens", Map.of(
                                        "access", token,
                                        "tokenRefresh", tokenRefresh
                                ),
                                "user", savedUser
                        )
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        // Valida email y contraseña contra la base de datos automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // Si pasa, busca el usuario y genera el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        final String token = jwtService.generateToken(userDetails);
        final String tokenRefresh = jwtService.generateRefreshToken(userDetails);

        final UsuarioResponseDto user = usuarioService.findByEmail(userDetails.getUsername());

        return ResponseEntity.ok(
                new AuthResponseDto(
                        Map.of(
                                "tokens", Map.of(
                                        "access", token,
                                        "tokenRefresh", tokenRefresh
                                ),
                                "user", user
                        )
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestHeader("refresh") String refresh) {

        // Si pasa, busca el usuario y genera el token
        final String userEmail = jwtService.extractUsername(refresh);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        final String token = jwtService.generateToken(userDetails);
        final String tokenRefresh = jwtService.generateRefreshToken(userDetails);

        final UsuarioResponseDto user = usuarioService.findByEmail(userDetails.getUsername());

        return ResponseEntity.ok(
                new AuthResponseDto(
                        Map.of(
                                "tokens", Map.of(
                                        "access", token,
                                        "tokenRefresh", tokenRefresh
                                ),
                                "user", user
                        )
                )
        );
    }
}