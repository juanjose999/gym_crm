package com.gymAdmin.auth.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        MetricasSuperioresDto metricasSuperiores,
        RendimientoIngresosYVentasDto rendimientoIngresosYVentas,
        MembresiasActivasDistribucionDto membresiasActivasDistribucion,
        List<MembresiaTablaDto> tablaMembresias,
        List<OrdenDto> ultimasOrdenes
) {

    public record MetricasSuperioresDto(
            MetricaDetalleDto ingresosDelMes,
            SocioMetricaDto sociosActivos,
            MetricaDetalleDto ventasHoy,
            SocioMetricaDto membresiasVigentes
    ) {}

    public record MetricaDetalleDto(
            String valor,
            String cambioPorcentaje,
            String comparativa
    ) {}

    public record SocioMetricaDto(
            int valor,
            String cambioPorcentaje,
            String detalle
    ) {}

    public record RendimientoIngresosYVentasDto(
            String periodo,
            String totalSemana,
            List<DiaGraficoDto> graficoDias
    ) {}

    public record DiaGraficoDto(
            String dia,
            BigDecimal ingresos,
            Boolean activo // true solo para el día actual; null en el resto
    ) {}

    public record MembresiasActivasDistribucionDto(
            String titulo,
            List<PlanDistribucionDto> planes
    ) {}

    public record PlanDistribucionDto(
            String nombre,
            int cantidadSocios
    ) {}

    public record MembresiaTablaDto(
            String membresia,
            String socios,
            String precio,
            String estado
    ) {}

    public record OrdenDto(
            String orden,
            String socio,
            String producto,
            String monto,
            String estado,
            String fecha
    ) {}
}