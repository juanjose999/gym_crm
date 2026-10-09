package com.gymAdmin.orden;

import com.gymAdmin.orden.dto.OrdenRequest;
import com.gymAdmin.orden.dto.OrdenResponse;
import com.gymAdmin.producto.Producto;
import com.gymAdmin.producto.ProductoService;
import com.gymAdmin.usuario.SocioService;
import com.gymAdmin.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final SocioService socioService;
    private final ProductoService productoService;

    /**
     * Registra una compra de un socio del gimnasio y descuenta el stock de cada producto.
     * Si un producto se repite con el mismo estado, sus cantidades se suman en un solo ítem;
     * con estados distintos (parte pagada, parte en deuda) quedan como ítems separados.
     */
    @Transactional
    public OrdenResponse crear(Long gimnasioId, OrdenRequest request) {
        Usuario socio = socioService.obtenerEntidad(gimnasioId, request.socioId());

        Map<ClaveItem, Integer> cantidadPorItem = request.items().stream()
                .collect(Collectors.toMap(
                        i -> new ClaveItem(i.productoId(), i.estado()),
                        OrdenRequest.ItemRequest::cantidad,
                        Integer::sum,
                        LinkedHashMap::new));

        Set<Long> productoIds = cantidadPorItem.keySet().stream()
                .map(ClaveItem::productoId)
                .collect(Collectors.toSet());
        Map<Long, Producto> productos = productoService.obtenerEntidades(gimnasioId, productoIds);

        Orden orden = new Orden(socio);
        cantidadPorItem.forEach((clave, cantidad) -> {
            Producto producto = productos.get(clave.productoId());
            producto.descontarStock(cantidad);
            orden.agregarItem(new ItemOrden(producto, cantidad, clave.estado()));
        });

        // flush para que los ids y fechas de auditoría estén disponibles en la respuesta
        return OrdenMapper.toResponse(ordenRepository.saveAndFlush(orden));
    }

    public List<OrdenResponse> listar(Long gimnasioId) {
        return ordenRepository.findAllByUsuarioGimnasioIdOrderByCreatedAtDesc(gimnasioId).stream()
                .map(OrdenMapper::toResponse)
                .toList();
    }

    public List<OrdenResponse> listarPorSocio(Long socioId) {
        return ordenRepository.findAllByUsuarioIdOrderByCreatedAtDesc(socioId).stream()
                .map(OrdenMapper::toResponse)
                .toList();
    }

    private record ClaveItem(Long productoId, EstadoItemOrden estado) {
    }
}
