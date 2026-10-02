package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_deuda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDeuda {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deuda_id", nullable = false)
    private Deuda deuda;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String metodo_pago;

    @Column(nullable = false)
    private LocalDateTime fecha_pago;

    private String referencia;


}