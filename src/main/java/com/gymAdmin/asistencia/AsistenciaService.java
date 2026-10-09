package com.gymAdmin.asistencia;

import com.gymAdmin.asistencia.dto.AsistenciaRequest;
import com.gymAdmin.asistencia.dto.AsistenciaResponse;
import com.gymAdmin.common.exception.ConflictException;
import com.gymAdmin.usuario.SocioService;
import com.gymAdmin.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final SocioService socioService;

    @Transactional
    public AsistenciaResponse registrar(Long gimnasioId, AsistenciaRequest request) {
        Usuario socio = socioService.obtenerEntidadPorEmail(gimnasioId, request.email());
        LocalDateTime fechaEntrada = request.fechaEntrada() != null ? request.fechaEntrada() : LocalDateTime.now();

        if (asistenciaRepository.existsByUsuarioIdAndFechaEntrada(socio.getId(), fechaEntrada)) {
            throw new ConflictException("Ya existe una asistencia registrada para ese socio en esa fecha y hora");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setUsuario(socio);
        asistencia.setFechaEntrada(fechaEntrada);
        asistencia.setTipoAcceso(request.tipoAcceso());

        return AsistenciaMapper.toResponse(asistenciaRepository.save(asistencia));
    }

    public List<AsistenciaResponse> listar(Long gimnasioId) {
        return asistenciaRepository.findAllByUsuarioGimnasioIdOrderByFechaEntradaDesc(gimnasioId).stream()
                .map(AsistenciaMapper::toResponse)
                .toList();
    }
}
