package com.gymAdmin.gimnasio;

import com.gymAdmin.common.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cada administrador gestiona un gimnasio; todos los datos del negocio quedan aislados por gimnasio.
 */
@Entity
@Table(name = "gimnasios")
@Getter
@Setter
@NoArgsConstructor
public class Gimnasio extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Gimnasio(String nombre) {
        this.nombre = nombre;
    }
}
