package com.gymAdmin.auth;

import com.gymAdmin.auth.dto.DashboardResponse;
import com.gymAdmin.auth.dto.DashboardResponse.DiaGraficoDto;
import com.gymAdmin.auth.dto.DashboardResponse.MembresiaTablaDto;
import com.gymAdmin.auth.dto.DashboardResponse.MembresiasActivasDistribucionDto;
import com.gymAdmin.auth.dto.DashboardResponse.MetricaDetalleDto;
import com.gymAdmin.auth.dto.DashboardResponse.MetricasSuperioresDto;
import com.gymAdmin.auth.dto.DashboardResponse.OrdenDto;
import com.gymAdmin.auth.dto.DashboardResponse.PlanDistribucionDto;
import com.gymAdmin.auth.dto.DashboardResponse.RendimientoIngresosYVentasDto;
import com.gymAdmin.auth.dto.DashboardResponse.SocioMetricaDto;
import com.gymAdmin.membresia.Membresia;
import com.gymAdmin.membresia.MembresiaRepository;
import com.gymAdmin.orden.ItemOrden;
import com.gymAdmin.orden.Orden;
import com.gymAdmin.orden.OrdenRepository;
import com.gymAdmin.plan.PlanRepository;
import com.gymAdmin.usuario.Rol;
import com.gymAdmin.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Arma el resumen del gimnasio que ve el administrador al ingresar (signup / login).
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private static final Locale LOCALE = Locale.of("es", "CL");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final int DIAS_GRAFICO = 7;
    private static final int DIAS_POR_VENCER = 7;
    private static final int LIMITE_TABLA_MEMBRESIAS = 5;

    private final OrdenRepository ordenRepository;
    private final MembresiaRepository membresiaRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardResponse construir(Long gimnasioId) {
        LocalDate hoy = LocalDate.now();
        LocalDate haceUnMes = hoy.minusMonths(1);
        // Desde el inicio del mes anterior se cubre la comparativa mensual y el gráfico de 7 días
        LocalDateTime desde = haceUnMes.withDayOfMonth(1).atStartOfDay();

        // Ventas = órdenes de productos; ingresos = ventas + pagos de membresías
        Map<LocalDate, BigDecimal> ventasPorDia = new HashMap<>();
        ordenRepository.findAllByUsuarioGimnasioIdAndCreatedAtGreaterThanEqual(gimnasioId, desde)
                .forEach(o -> ventasPorDia.merge(o.getCreatedAt().toLocalDate(), o.getTotal(), BigDecimal::add));

        Map<LocalDate, BigDecimal> ingresosPorDia = new HashMap<>(ventasPorDia);
        membresiaRepository.findPagosByGimnasioIdDesde(gimnasioId, desde)
                .forEach(p -> ingresosPorDia.merge(p.getCreatedAt().toLocalDate(), p.getMonto(), BigDecimal::add));

        List<Membresia> membresias = membresiaRepository.findAllByUsuarioGimnasioIdOrderByFechaInicioDesc(gimnasioId);
        List<Membresia> vigentes = vigentesEn(membresias, hoy);

        return new DashboardResponse(
                metricasSuperiores(gimnasioId, hoy, ventasPorDia, ingresosPorDia, membresias, vigentes),
                rendimiento(hoy, ingresosPorDia),
                distribucion(gimnasioId, vigentes),
                tablaMembresias(membresias),
                ultimasOrdenes(gimnasioId)
        );
    }

    private MetricasSuperioresDto metricasSuperiores(Long gimnasioId, LocalDate hoy,
                                                     Map<LocalDate, BigDecimal> ventasPorDia,
                                                     Map<LocalDate, BigDecimal> ingresosPorDia,
                                                     List<Membresia> membresias,
                                                     List<Membresia> vigentes) {
        LocalDate haceUnMes = hoy.minusMonths(1);
        List<Membresia> vigentesHaceUnMes = vigentesEn(membresias, haceUnMes);

        // Se compara contra el mismo tramo del mes anterior (del día 1 al mismo día del mes)
        BigDecimal ingresosMes = sumar(ingresosPorDia, hoy.withDayOfMonth(1), hoy);
        BigDecimal ingresosMesAnterior = sumar(ingresosPorDia, haceUnMes.withDayOfMonth(1), haceUnMes);

        BigDecimal ventasHoy = ventasPorDia.getOrDefault(hoy, BigDecimal.ZERO);
        BigDecimal ventasAyer = ventasPorDia.getOrDefault(hoy.minusDays(1), BigDecimal.ZERO);

        int sociosActivos = contarSocios(vigentes);
        long sociosNuevos = usuarioRepository.countByGimnasioIdAndRolAndCreatedAtGreaterThanEqual(
                gimnasioId, Rol.SOCIO, hoy.withDayOfMonth(1).atStartOfDay());

        LocalDate limiteVencimiento = hoy.plusDays(DIAS_POR_VENCER);
        long porVencer = vigentes.stream()
                .filter(m -> !m.getFechaFin().isAfter(limiteVencimiento))
                .count();

        return new MetricasSuperioresDto(
                new MetricaDetalleDto(
                        moneda(ingresosMes),
                        cambioPorcentaje(ingresosMes, ingresosMesAnterior),
                        "vs mismo periodo del mes anterior"),
                new SocioMetricaDto(
                        sociosActivos,
                        cambioPorcentaje(sociosActivos, contarSocios(vigentesHaceUnMes)),
                        sociosNuevos + " nuevos este mes"),
                new MetricaDetalleDto(
                        moneda(ventasHoy),
                        cambioPorcentaje(ventasHoy, ventasAyer),
                        "vs ayer"),
                new SocioMetricaDto(
                        vigentes.size(),
                        cambioPorcentaje(vigentes.size(), vigentesHaceUnMes.size()),
                        porVencer + " vencen en los próximos " + DIAS_POR_VENCER + " días")
        );
    }

    private RendimientoIngresosYVentasDto rendimiento(LocalDate hoy, Map<LocalDate, BigDecimal> ingresosPorDia) {
        List<DiaGraficoDto> dias = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = DIAS_GRAFICO - 1; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            BigDecimal ingresos = ingresosPorDia.getOrDefault(dia, BigDecimal.ZERO);
            total = total.add(ingresos);
            dias.add(new DiaGraficoDto(
                    dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, LOCALE),
                    ingresos,
                    dia.equals(hoy) ? Boolean.TRUE : null));
        }

        return new RendimientoIngresosYVentasDto("Últimos " + DIAS_GRAFICO + " días", moneda(total), dias);
    }

    private MembresiasActivasDistribucionDto distribucion(Long gimnasioId, List<Membresia> vigentes) {
        Map<Long, Set<Long>> sociosPorPlan = new HashMap<>();
        vigentes.forEach(m -> sociosPorPlan
                .computeIfAbsent(m.getPlan().getId(), id -> new HashSet<>())
                .add(m.getUsuario().getId()));

        List<PlanDistribucionDto> planes = planRepository.findAllByGimnasioIdOrderByNombreAsc(gimnasioId).stream()
                .map(plan -> new PlanDistribucionDto(
                        plan.getNombre(),
                        sociosPorPlan.getOrDefault(plan.getId(), Set.of()).size()))
                .toList();

        return new MembresiasActivasDistribucionDto("Membresías activas por plan", planes);
    }

    /** Membresías más recientes (por fecha de inicio). */
    private List<MembresiaTablaDto> tablaMembresias(List<Membresia> membresias) {
        return membresias.stream()
                .limit(LIMITE_TABLA_MEMBRESIAS)
                .map(m -> new MembresiaTablaDto(
                        m.getPlan().getNombre(),
                        m.getUsuario().getNombreCompleto(),
                        moneda(m.getPlan().getPrecio()),
                        m.getEstado().name()))
                .toList();
    }

    private List<OrdenDto> ultimasOrdenes(Long gimnasioId) {
        return ordenRepository.findTop5ByUsuarioGimnasioIdOrderByCreatedAtDesc(gimnasioId).stream()
                .map(o -> new OrdenDto(
                        "#" + o.getId(),
                        o.getUsuario().getNombreCompleto(),
                        describirProductos(o),
                        moneda(o.getTotal()),
                        "COMPLETADA",
                        o.getCreatedAt().format(FORMATO_FECHA)))
                .toList();
    }

    /** Nombre del primer producto y, si hay más, cuántos otros incluye (p. ej. "Proteína +2"). */
    private static String describirProductos(Orden orden) {
        List<ItemOrden> items = orden.getItems();
        if (items.isEmpty()) {
            return "";
        }
        String primero = items.getFirst().getProducto().getNombre();
        return items.size() > 1 ? primero + " +" + (items.size() - 1) : primero;
    }

    private static List<Membresia> vigentesEn(List<Membresia> membresias, LocalDate fecha) {
        return membresias.stream()
                .filter(m -> !m.getFechaInicio().isAfter(fecha) && !m.getFechaFin().isBefore(fecha))
                .toList();
    }

    private static int contarSocios(List<Membresia> membresias) {
        return (int) membresias.stream().map(m -> m.getUsuario().getId()).distinct().count();
    }

    private static BigDecimal sumar(Map<LocalDate, BigDecimal> porDia, LocalDate desde, LocalDate hasta) {
        return porDia.entrySet().stream()
                .filter(e -> !e.getKey().isBefore(desde) && !e.getKey().isAfter(hasta))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static String cambioPorcentaje(long actual, long anterior) {
        return cambioPorcentaje(BigDecimal.valueOf(actual), BigDecimal.valueOf(anterior));
    }

    private static String cambioPorcentaje(BigDecimal actual, BigDecimal anterior) {
        if (anterior.signum() == 0) {
            return actual.signum() == 0 ? "0%" : "+100%";
        }
        BigDecimal cambio = actual.subtract(anterior)
                .multiply(CIEN)
                .divide(anterior, 1, RoundingMode.HALF_UP)
                .stripTrailingZeros();
        return (cambio.signum() > 0 ? "+" : "") + cambio.toPlainString() + "%";
    }

    private static String moneda(BigDecimal monto) {
        // NumberFormat no es thread-safe, por eso se crea en cada uso
        return NumberFormat.getCurrencyInstance(LOCALE).format(monto);
    }
}
