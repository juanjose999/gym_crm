package com.gymAdmin.producto;

import com.gymAdmin.common.exception.ResourceNotFoundException;
import com.gymAdmin.gimnasio.GimnasioRepository;
import com.gymAdmin.producto.dto.ProductoRequest;
import com.gymAdmin.producto.dto.ProductoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final GimnasioRepository gimnasioRepository;

    @Transactional
    public ProductoResponse crear(Long gimnasioId, ProductoRequest request) {
        Producto producto = ProductoMapper.toEntity(request);
        producto.setGimnasio(gimnasioRepository.getReferenceById(gimnasioId));
        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    public List<ProductoResponse> listar(Long gimnasioId) {
        return productoRepository.findAllByGimnasioIdOrderByNombreAsc(gimnasioId).stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    public ProductoResponse buscarPorId(Long gimnasioId, Long id) {
        return ProductoMapper.toResponse(obtenerEntidad(gimnasioId, id));
    }

    @Transactional
    public ProductoResponse actualizar(Long gimnasioId, Long id, ProductoRequest request) {
        Producto producto = obtenerEntidad(gimnasioId, id);
        ProductoMapper.actualizar(producto, request);
        return ProductoMapper.toResponse(producto);
    }

    @Transactional
    public void eliminar(Long gimnasioId, Long id) {
        productoRepository.delete(obtenerEntidad(gimnasioId, id));
    }

    public Producto obtenerEntidad(Long gimnasioId, Long id) {
        return productoRepository.findByIdAndGimnasioId(id, gimnasioId)
                .orElseThrow(() -> ResourceNotFoundException.of("Producto", id));
    }

    /**
     * Devuelve los productos del gimnasio indexados por id; falla si alguno no existe.
     */
    public Map<Long, Producto> obtenerEntidades(Long gimnasioId, Collection<Long> ids) {
        Map<Long, Producto> productos = productoRepository.findAllByIdInAndGimnasioId(ids, gimnasioId).stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));

        List<Long> faltantes = ids.stream().filter(id -> !productos.containsKey(id)).toList();
        if (!faltantes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron los productos con id: " + faltantes);
        }
        return productos;
    }
}
