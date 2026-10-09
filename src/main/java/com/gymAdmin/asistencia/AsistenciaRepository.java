package com.gymAdmin.asistencia;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    boolean existsByUsuarioIdAndFechaEntrada(Long usuarioId, LocalDateTime fechaEntrada);

    @EntityGraph(attributePaths = "usuario")
    List<Asistencia> findAllByUsuarioGimnasioIdOrderByFechaEntradaDesc(Long gimnasioId);
}
