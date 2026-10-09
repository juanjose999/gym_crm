package com.gymAdmin.plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findAllByGimnasioIdOrderByNombreAsc(Long gimnasioId);

    Optional<Plan> findByIdAndGimnasioId(Long id, Long gimnasioId);
}
