package com.gymAdmin.orden;

import com.gymAdmin.orden.dto.OrdenResponse;

public final class OrdenMapper {

    private OrdenMapper() {
    }

    public static OrdenResponse toResponse(Orden orden) {
        return new OrdenResponse(
                orden.getId(),
                orden.getUsuario().getId(),
                orden.getUsuario().getNombreCompleto(),
                orden.getCreatedAt(),
                orden.getTotal(),
                orden.getItems().stream().map(OrdenMapper::toItemResponse).toList()
        );
    }

    private static OrdenResponse.ItemResponse toItemResponse(ItemOrden item) {
        return new OrdenResponse.ItemResponse(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getSubtotal()
        );
    }
}
