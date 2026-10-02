package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "membresias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membresia {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanMembresia plan;

    @Column(nullable = false)
    private LocalDate fecha_inicio;

    @Column(nullable = false)
    private LocalDate fecha_fin;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private LocalDateTime fecha_creacion;

    @OneToMany(mappedBy = "membresia")
    private List<PagoMembresia> pagos;


}
