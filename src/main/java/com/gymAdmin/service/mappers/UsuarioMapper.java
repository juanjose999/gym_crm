package com.gymAdmin.service.mappers;

import com.gymAdmin.entity.Usuario;
import com.gymAdmin.service.dtos.UsuarioRequestDto;
import com.gymAdmin.service.dtos.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {
    
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static Usuario toEntity(UsuarioRequestDto requestDTO) {
        if(requestDTO == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNombres(requestDTO.nombres());
        usuario.setApellidos(requestDTO.apellidos());
        usuario.setTelefono(requestDTO.telefono());
        usuario.setEmail(requestDTO.email());
        usuario.setPassword(passwordEncoder.encode(requestDTO.password()));
        return usuario;
    }

    public static UsuarioResponseDto toResponse(Usuario usuario) {
        if(usuario == null) return null;

        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
        return usuarioResponseDto;
    }

}
