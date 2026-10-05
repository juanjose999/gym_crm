package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "planes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Integer duracion_dias;

    @Column(nullable = false)
    private Double precio;

    @OneToMany(mappedBy = "plan")
    private List<Membresia> membresias;


}