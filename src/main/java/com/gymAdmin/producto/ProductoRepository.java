package com.gymAdmin.producto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByGimnasioIdOrderByNombreAsc(Long gimnasioId);

    Optional<Producto> findByIdAndGimnasioId(Long id, Long gimnasioId);

    List<Producto> findAllByIdInAndGimnasioId(Collection<Long> ids, Long gimnasioId);
}
