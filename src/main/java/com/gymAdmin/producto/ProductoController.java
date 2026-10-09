package com.gymAdmin.producto;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.producto.dto.ProductoRequest;
import com.gymAdmin.producto.dto.ProductoResponse;
import com.gymAdmin.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductoResponse> crear(@AuthenticationPrincipal UsuarioAutenticado admin,
                                               @Valid @RequestBody ProductoRequest request) {
        return ApiResponse.success(productoService.crear(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<ProductoResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(productoService.listar(admin.gimnasioId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductoResponse> buscarPorId(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                     @PathVariable Long id) {
        return ApiResponse.success(productoService.buscarPorId(admin.gimnasioId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductoResponse> actualizar(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                    @PathVariable Long id,
                                                    @Valid @RequestBody ProductoRequest request) {
        return ApiResponse.success(productoService.actualizar(admin.gimnasioId(), id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@AuthenticationPrincipal UsuarioAutenticado admin, @PathVariable Long id) {
        productoService.eliminar(admin.gimnasioId(), id);
    }
}
