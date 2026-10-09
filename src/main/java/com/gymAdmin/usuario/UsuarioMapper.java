package com.gymAdmin.usuario;

import com.gymAdmin.usuario.dto.SocioRequest;
import com.gymAdmin.usuario.dto.UsuarioResponse;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toSocio(SocioRequest request) {
        Usuario socio = new Usuario();
        socio.setRol(Rol.SOCIO);
        actualizar(socio, request);
        return socio;
    }

    public static void actualizar(Usuario usuario, SocioRequest request) {
        usuario.setNombres(request.nombres());
        usuario.setApellidos(request.apellidos());
        usuario.setTelefono(request.telefono());
        usuario.setEmail(request.email());
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }
}
