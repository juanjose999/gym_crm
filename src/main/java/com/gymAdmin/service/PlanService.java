package com.gymAdmin.service;

import com.gymAdmin.entity.Plan;
import com.gymAdmin.repository.PlanRepository;
import com.gymAdmin.service.dtos.PlanRequestDto;
import com.gymAdmin.service.dtos.PlanResponseDto;
import com.gymAdmin.service.mappers.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public PlanResponseDto save(PlanRequestDto planRequestDto) {
        Plan savedPlan = PlanMapper.toEntity(planRequestDto);
        savedPlan = planRepository.save(savedPlan);

        return PlanMapper.toPlanResponsetDto(savedPlan);
    }

    public List<PlanResponseDto> findAll() {
        return planRepository.findAll()
                .stream().map(
                        p -> PlanMapper.toPlanResponsetDto(p))
                .collect(Collectors.toList());

    }

    public PlanResponseDto findById(Long id) {
        Plan responseDto = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        return PlanMapper.toPlanResponsetDto(responseDto);
    }

    public PlanResponseDto update(PlanRequestDto plan, Long id) {

        Plan planExistente = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        planExistente.setNombre(plan.nombre());
        planExistente.setDescripcion(plan.descripcion());
        planExistente.setDuracion_dias(plan.duracionDias());
        planExistente.setPrecio(plan.precio());
        planRepository.save(planExistente);

        return PlanMapper.toPlanResponsetDto(planExistente);
    }

    public void delete(Long id) {
        Plan planExistente = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
        planRepository.deleteById(id);
    }

}
