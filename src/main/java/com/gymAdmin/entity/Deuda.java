package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "deudas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deuda {

    @Id
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha_deuda;

    private LocalDate fecha_vencimiento;

    private String observaciones;

    @OneToMany(mappedBy = "deuda")
    private List<DetalleDeuda> detalles;

    @OneToMany(mappedBy = "deuda")
    private List<PagoDeuda> pagos;

}
