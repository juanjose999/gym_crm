package com.gymAdmin.security;

import com.gymAdmin.usuario.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Usuario de la petición actual. Se inyecta en los controladores con {@code @AuthenticationPrincipal}
 * para saber a qué gimnasio pertenece y limitar los datos.
 *
 * @param password nulo para los socios, que no tienen contraseña
 */
public record UsuarioAutenticado(
        Long id,
        String email,
        String password,
        Rol rol,
        Long gimnasioId
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
