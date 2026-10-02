package com.gymAdmin.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Column(nullable = false, unique = true)
    private String nombre;

    @Id
    private Long id;

}