package com.gymAdmin.membresia;

import com.gymAdmin.common.exception.BusinessException;
import com.gymAdmin.common.exception.ResourceNotFoundException;
import com.gymAdmin.membresia.dto.MembresiaRequest;
import com.gymAdmin.membresia.dto.MembresiaResponse;
import com.gymAdmin.membresia.dto.MembresiaUpdateRequest;
import com.gymAdmin.membresia.dto.PagoMembresiaRequest;
import com.gymAdmin.plan.Plan;
import com.gymAdmin.plan.PlanService;
import com.gymAdmin.usuario.SocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembresiaService {

    private final MembresiaRepository membresiaRepository;
    private final SocioService socioService;
    private final PlanService planService;

    @Transactional
    public MembresiaResponse crear(Long gimnasioId, MembresiaRequest request) {
        Plan plan = planService.obtenerEntidad(gimnasioId, request.planId());

        Membresia membresia = new Membresia();
        membresia.setUsuario(socioService.obtenerEntidad(gimnasioId, request.socioId()));
        membresia.setPlan(plan);
        asignarVigencia(membresia, plan, request.fechaInicio(), request.fechaFin());

        if (request.monto() != null && request.monto().signum() > 0) {
            if (request.metodoPago() == null || request.metodoPago().isBlank()) {
                throw new BusinessException("El método de pago es obligatorio cuando se informa un monto");
            }
            membresia.agregarPago(new PagoMembresia(request.monto(), request.metodoPago()));
        }
        membresia.actualizarEstado();

        // flush para que los ids y fechas de auditoría estén disponibles en la respuesta
        return MembresiaMapper.toResponse(membresiaRepository.saveAndFlush(membresia));
    }

    public List<MembresiaResponse> listar(Long gimnasioId) {
        return membresiaRepository.findAllByUsuarioGimnasioIdOrderByFechaInicioDesc(gimnasioId).stream()
                .map(MembresiaMapper::toResponse)
                .toList();
    }

    public List<MembresiaResponse> listarPorSocio(Long socioId) {
        return membresiaRepository.findAllByUsuarioIdOrderByFechaInicioDesc(socioId).stream()
                .map(MembresiaMapper::toResponse)
                .toList();
    }

    public MembresiaResponse buscarPorId(Long gimnasioId, Long id) {
        return MembresiaMapper.toResponse(obtenerEntidad(gimnasioId, id));
    }

    @Transactional
    public MembresiaResponse actualizar(Long gimnasioId, Long id, MembresiaUpdateRequest request) {
        Membresia membresia = obtenerEntidad(gimnasioId, id);
        Plan plan = planService.obtenerEntidad(gimnasioId, request.planId());

        membresia.setPlan(plan);
        asignarVigencia(membresia, plan, request.fechaInicio(), request.fechaFin());
        // Un cambio de plan puede cambiar el precio y, con él, el estado
        membresia.actualizarEstado();

        return MembresiaMapper.toResponse(membresia);
    }

    @Transactional
    public void eliminar(Long gimnasioId, Long id) {
        membresiaRepository.delete(obtenerEntidad(gimnasioId, id));
    }

    @Transactional
    public MembresiaResponse registrarPago(Long gimnasioId, Long membresiaId, PagoMembresiaRequest request) {
        Membresia membresia = obtenerEntidad(gimnasioId, membresiaId);
        membresia.agregarPago(new PagoMembresia(request.monto(), request.metodoPago()));

        return MembresiaMapper.toResponse(membresiaRepository.saveAndFlush(membresia));
    }

    @Transactional
    public MembresiaResponse actualizarPago(Long gimnasioId, Long membresiaId, Long pagoId,
                                            PagoMembresiaRequest request) {
        Membresia membresia = obtenerEntidad(gimnasioId, membresiaId);

        PagoMembresia pago = membresia.getPagos().stream()
                .filter(p -> p.getId().equals(pagoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago " + pagoId + " no encontrado en la membresía " + membresiaId));

        pago.setMonto(request.monto());
        pago.setMetodoPago(request.metodoPago());
        membresia.actualizarEstado();

        return MembresiaMapper.toResponse(membresia);
    }

    private Membresia obtenerEntidad(Long gimnasioId, Long id) {
        return membresiaRepository.findByIdAndUsuarioGimnasioId(id, gimnasioId)
                .orElseThrow(() -> ResourceNotFoundException.of("Membresía", id));
    }

    /**
     * Si no se envía fecha de fin, se calcula a partir de la duración del plan.
     */
    private void asignarVigencia(Membresia membresia, Plan plan, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate fin = fechaFin != null ? fechaFin : fechaInicio.plusDays(plan.getDuracionDias());
        if (fin.isBefore(fechaInicio)) {
            throw new BusinessException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        membresia.setFechaInicio(fechaInicio);
        membresia.setFechaFin(fin);
    }
}
