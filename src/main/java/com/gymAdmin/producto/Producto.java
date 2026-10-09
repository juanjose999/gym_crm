package com.gymAdmin.producto;

import com.gymAdmin.common.exception.BusinessException;
import com.gymAdmin.common.persistence.AuditableEntity;
import com.gymAdmin.gimnasio.Gimnasio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gimnasio_id", nullable = false)
    private Gimnasio gimnasio;

    /** Bloqueo optimista: evita que dos compras simultáneas descuenten el mismo stock. */
    @Version
    private Long version;

    public void descontarStock(int cantidad) {
        if (cantidad > stock) {
            throw new BusinessException("Stock insuficiente para '%s'. Disponible: %d, solicitado: %d"
                    .formatted(nombre, stock, cantidad));
        }
        stock -= cantidad;
    }
}
