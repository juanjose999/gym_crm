package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.node.StringNode;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_membresia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoMembresia extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membresia_id", nullable = false)
    private Membresia membresia;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String metodo_pago;


}