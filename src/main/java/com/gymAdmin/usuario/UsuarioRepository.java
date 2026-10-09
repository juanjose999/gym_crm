package com.gymAdmin.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndRol(String email, Rol rol);

    boolean existsByEmail(String email);

    Optional<Usuario> findByIdAndGimnasioIdAndRol(Long id, Long gimnasioId, Rol rol);

    Optional<Usuario> findByEmailAndGimnasioIdAndRol(String email, Long gimnasioId, Rol rol);

    List<Usuario> findAllByGimnasioIdAndRolOrderByApellidosAscNombresAsc(Long gimnasioId, Rol rol);
}
