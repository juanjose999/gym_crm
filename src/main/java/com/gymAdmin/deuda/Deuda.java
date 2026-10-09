package com.gymAdmin.deuda;

import com.gymAdmin.common.persistence.AuditableEntity;
import com.gymAdmin.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deudas")
@Getter
@Setter
@NoArgsConstructor
public class Deuda extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaDeuda;

    private LocalDate fechaVencimiento;

    private String observaciones;

    @OneToMany(mappedBy = "deuda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleDeuda> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "deuda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoDeuda> pagos = new ArrayList<>();
}
