package com.gymAdmin.usuario;

import com.gymAdmin.common.exception.ResourceNotFoundException;
import com.gymAdmin.gimnasio.GimnasioRepository;
import com.gymAdmin.usuario.dto.SocioRequest;
import com.gymAdmin.usuario.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gestión de socios por parte del administrador. Todas las operaciones quedan limitadas a su gimnasio.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocioService {

    private final UsuarioRepository usuarioRepository;
    private final GimnasioRepository gimnasioRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public UsuarioResponse crear(Long gimnasioId, SocioRequest request) {
        usuarioService.validarEmailDisponible(request.email());

        Usuario socio = UsuarioMapper.toSocio(request);
        socio.setGimnasio(gimnasioRepository.getReferenceById(gimnasioId));
        return UsuarioMapper.toResponse(usuarioRepository.save(socio));
    }

    public List<UsuarioResponse> listar(Long gimnasioId) {
        return usuarioRepository.findAllByGimnasioIdAndRolOrderByApellidosAscNombresAsc(gimnasioId, Rol.SOCIO).stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long gimnasioId, Long id) {
        return UsuarioMapper.toResponse(obtenerEntidad(gimnasioId, id));
    }

    @Transactional
    public UsuarioResponse actualizar(Long gimnasioId, Long id, SocioRequest request) {
        Usuario socio = obtenerEntidad(gimnasioId, id);
        if (!socio.getEmail().equals(request.email())) {
            usuarioService.validarEmailDisponible(request.email());
        }
        UsuarioMapper.actualizar(socio, request);
        return UsuarioMapper.toResponse(socio);
    }

    @Transactional
    public void eliminar(Long gimnasioId, Long id) {
        usuarioRepository.delete(obtenerEntidad(gimnasioId, id));
    }

    public Usuario obtenerEntidad(Long gimnasioId, Long id) {
        return usuarioRepository.findByIdAndGimnasioIdAndRol(id, gimnasioId, Rol.SOCIO)
                .orElseThrow(() -> ResourceNotFoundException.of("Socio", id));
    }

    public Usuario obtenerEntidadPorEmail(Long gimnasioId, String email) {
        return usuarioRepository.findByEmailAndGimnasioIdAndRol(email, gimnasioId, Rol.SOCIO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un socio con el correo: " + email));
    }
}
