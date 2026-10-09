package com.gymAdmin.usuario;

import com.gymAdmin.common.exception.ConflictException;
import com.gymAdmin.common.exception.ResourceNotFoundException;
import com.gymAdmin.gimnasio.Gimnasio;
import com.gymAdmin.gimnasio.GimnasioRepository;
import com.gymAdmin.usuario.dto.RegistroAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Operaciones de cuenta comunes a administradores y socios (registro, búsqueda por correo).
 * La gestión de socios por parte del admin está en {@link SocioService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final GimnasioRepository gimnasioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea el gimnasio y su usuario administrador.
     */
    @Transactional
    public Usuario registrarAdmin(RegistroAdminRequest request) {
        validarEmailDisponible(request.email());

        Gimnasio gimnasio = gimnasioRepository.save(new Gimnasio(request.nombreGimnasio()));

        Usuario admin = new Usuario();
        admin.setNombres(request.nombres());
        admin.setApellidos(request.apellidos());
        admin.setTelefono(request.telefono());
        admin.setEmail(request.email());
        admin.setPassword(passwordEncoder.encode(request.password()));
        admin.setRol(Rol.ADMIN);
        admin.setGimnasio(gimnasio);
        return usuarioRepository.save(admin);
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el correo: " + email));
    }

    public Usuario obtenerSocioPorEmail(String email) {
        return usuarioRepository.findByEmailAndRol(email, Rol.SOCIO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un socio registrado con el correo: " + email));
    }

    public void validarEmailDisponible(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictException("Ya existe un usuario registrado con el correo: " + email);
        }
    }
}
