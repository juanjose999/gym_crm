package com.gymAdmin.membresia;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembresiaRepository extends JpaRepository<Membresia, Long> {

    @EntityGraph(attributePaths = {"usuario", "plan"})
    List<Membresia> findAllByUsuarioGimnasioIdOrderByFechaInicioDesc(Long gimnasioId);

    Optional<Membresia> findByIdAndUsuarioGimnasioId(Long id, Long gimnasioId);

    @EntityGraph(attributePaths = {"usuario", "plan"})
    List<Membresia> findAllByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
}
