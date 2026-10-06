package com.gymAdmin.repository;

import com.gymAdmin.entity.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresiaRepositoy extends JpaRepository<Membresia, Integer> {
}
