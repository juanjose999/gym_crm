package com.gymAdmin.membresia;

public enum EstadoMembresia {
    /** No se ha registrado ningún pago. */
    DEUDA,
    /** Hay pagos, pero no cubren el precio del plan. */
    PAGO_PARCIAL,
    /** Los pagos cubren el precio del plan. */
    PAGADO
}
