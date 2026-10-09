package com.gymAdmin.membresia;

import com.gymAdmin.common.persistence.AuditableEntity;
import com.gymAdmin.plan.Plan;
import com.gymAdmin.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membresias")
@Getter
@Setter
@NoArgsConstructor
public class Membresia extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "membresia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoMembresia> pagos = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMembresia estado = EstadoMembresia.DEUDA;

    public void agregarPago(PagoMembresia pago) {
        pago.setMembresia(this);
        pagos.add(pago);
        actualizarEstado();
    }

    public BigDecimal getTotalPagado() {
        return pagos.stream()
                .map(PagoMembresia::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSaldoPendiente() {
        return plan.getPrecio().subtract(getTotalPagado()).max(BigDecimal.ZERO);
    }

    /**
     * Recalcula el estado comparando el total pagado con el precio del plan.
     * Debe llamarse cada vez que cambian los pagos o el plan.
     */
    public void actualizarEstado() {
        BigDecimal totalPagado = getTotalPagado();

        if (totalPagado.signum() <= 0) {
            estado = EstadoMembresia.DEUDA;
        } else if (totalPagado.compareTo(plan.getPrecio()) < 0) {
            estado = EstadoMembresia.PAGO_PARCIAL;
        } else {
            estado = EstadoMembresia.PAGADO;
        }
    }
}
