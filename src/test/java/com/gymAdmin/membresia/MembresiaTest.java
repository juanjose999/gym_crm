package com.gymAdmin.membresia;

import com.gymAdmin.plan.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MembresiaTest {

    private Membresia membresia;

    @BeforeEach
    void setUp() {
        Plan plan = new Plan();
        plan.setPrecio(new BigDecimal("100.00"));

        membresia = new Membresia();
        membresia.setPlan(plan);
    }

    @Test
    void sinPagosQuedaEnDeuda() {
        membresia.actualizarEstado();

        assertEquals(EstadoMembresia.DEUDA, membresia.getEstado());
        assertEquals(new BigDecimal("100.00"), membresia.getSaldoPendiente());
    }

    @Test
    void pagoMenorAlPrecioQuedaEnPagoParcial() {
        membresia.agregarPago(new PagoMembresia(new BigDecimal("40.00"), "EFECTIVO"));

        assertEquals(EstadoMembresia.PAGO_PARCIAL, membresia.getEstado());
        assertEquals(new BigDecimal("60.00"), membresia.getSaldoPendiente());
    }

    @Test
    void pagosQueCubrenElPrecioQuedanPagados() {
        membresia.agregarPago(new PagoMembresia(new BigDecimal("40.00"), "EFECTIVO"));
        membresia.agregarPago(new PagoMembresia(new BigDecimal("60.00"), "TARJETA"));

        assertEquals(EstadoMembresia.PAGADO, membresia.getEstado());
        assertEquals(0, membresia.getSaldoPendiente().signum());
    }

    @Test
    void editarUnPagoRecalculaElEstado() {
        PagoMembresia pago = new PagoMembresia(new BigDecimal("100.00"), "EFECTIVO");
        membresia.agregarPago(pago);

        pago.setMonto(new BigDecimal("30.00"));
        membresia.actualizarEstado();

        assertEquals(EstadoMembresia.PAGO_PARCIAL, membresia.getEstado());
    }

    @Test
    void cambiarAUnPlanMasCaroRecalculaElEstado() {
        membresia.agregarPago(new PagoMembresia(new BigDecimal("100.00"), "EFECTIVO"));
        Plan planMasCaro = new Plan();
        planMasCaro.setPrecio(new BigDecimal("150.00"));

        membresia.setPlan(planMasCaro);
        membresia.actualizarEstado();

        assertEquals(EstadoMembresia.PAGO_PARCIAL, membresia.getEstado());
    }
}
