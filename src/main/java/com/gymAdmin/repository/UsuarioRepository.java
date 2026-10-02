package com.gymAdmin.repository;

import com.gymAdmin.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Boolean deleteByEmail(String email);
}
