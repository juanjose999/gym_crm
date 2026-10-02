package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "articulos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Articulo {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private LocalDateTime fecha_creacion;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleDeuda> detalles_deuda;

}