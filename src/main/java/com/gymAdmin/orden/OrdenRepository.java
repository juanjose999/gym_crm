package com.gymAdmin.orden;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @EntityGraph(attributePaths = {"usuario", "items", "items.producto"})
    List<Orden> findAllByUsuarioGimnasioIdOrderByCreatedAtDesc(Long gimnasioId);

    @EntityGraph(attributePaths = {"usuario", "items", "items.producto"})
    List<Orden> findAllByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
}
