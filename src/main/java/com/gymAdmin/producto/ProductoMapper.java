package com.gymAdmin.producto;

import com.gymAdmin.producto.dto.ProductoRequest;
import com.gymAdmin.producto.dto.ProductoResponse;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        actualizar(producto, request);
        return producto;
    }

    public static void actualizar(Producto producto, ProductoRequest request) {
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
    }

    public static ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
