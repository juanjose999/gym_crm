package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_deuda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleDeuda {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deuda_id", nullable = false)
    private Deuda deuda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio_unitario;

    @Column(insertable = false, updatable = false)
    private Double subtotal;


}
