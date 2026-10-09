package com.gymAdmin.plan;

import com.gymAdmin.common.exception.ResourceNotFoundException;
import com.gymAdmin.gimnasio.GimnasioRepository;
import com.gymAdmin.plan.dto.PlanRequest;
import com.gymAdmin.plan.dto.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;
    private final GimnasioRepository gimnasioRepository;

    @Transactional
    public PlanResponse crear(Long gimnasioId, PlanRequest request) {
        Plan plan = PlanMapper.toEntity(request);
        plan.setGimnasio(gimnasioRepository.getReferenceById(gimnasioId));
        return PlanMapper.toResponse(planRepository.save(plan));
    }

    public List<PlanResponse> listar(Long gimnasioId) {
        return planRepository.findAllByGimnasioIdOrderByNombreAsc(gimnasioId).stream()
                .map(PlanMapper::toResponse)
                .toList();
    }

    public PlanResponse buscarPorId(Long gimnasioId, Long id) {
        return PlanMapper.toResponse(obtenerEntidad(gimnasioId, id));
    }

    @Transactional
    public PlanResponse actualizar(Long gimnasioId, Long id, PlanRequest request) {
        Plan plan = obtenerEntidad(gimnasioId, id);
        PlanMapper.actualizar(plan, request);
        return PlanMapper.toResponse(plan);
    }

    @Transactional
    public void eliminar(Long gimnasioId, Long id) {
        planRepository.delete(obtenerEntidad(gimnasioId, id));
    }

    public Plan obtenerEntidad(Long gimnasioId, Long id) {
        return planRepository.findByIdAndGimnasioId(id, gimnasioId)
                .orElseThrow(() -> ResourceNotFoundException.of("Plan", id));
    }
}
