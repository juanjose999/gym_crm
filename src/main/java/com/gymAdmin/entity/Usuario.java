package com.gymAdmin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private String telefono;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "usuario")
    private List<Membresia> membresias;

    @OneToMany(mappedBy = "usuario")
    private List<Deuda> deudas;

    @OneToMany(mappedBy = "usuario")
    private List<Asistencia> asistencias;

}