package com.gymAdmin.membresia;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.membresia.dto.MembresiaRequest;
import com.gymAdmin.membresia.dto.MembresiaResponse;
import com.gymAdmin.membresia.dto.MembresiaUpdateRequest;
import com.gymAdmin.membresia.dto.PagoMembresiaRequest;
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
@RequestMapping("/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MembresiaResponse> crear(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                @Valid @RequestBody MembresiaRequest request) {
        return ApiResponse.success(membresiaService.crear(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<MembresiaResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(membresiaService.listar(admin.gimnasioId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<MembresiaResponse> buscarPorId(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                      @PathVariable Long id) {
        return ApiResponse.success(membresiaService.buscarPorId(admin.gimnasioId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<MembresiaResponse> actualizar(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody MembresiaUpdateRequest request) {
        return ApiResponse.success(membresiaService.actualizar(admin.gimnasioId(), id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@AuthenticationPrincipal UsuarioAutenticado admin, @PathVariable Long id) {
        membresiaService.eliminar(admin.gimnasioId(), id);
    }

    @PostMapping("/{id}/pagos")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MembresiaResponse> registrarPago(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody PagoMembresiaRequest request) {
        return ApiResponse.success(membresiaService.registrarPago(admin.gimnasioId(), id, request));
    }

    @PutMapping("/{id}/pagos/{pagoId}")
    public ApiResponse<MembresiaResponse> actualizarPago(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                         @PathVariable Long id,
                                                         @PathVariable Long pagoId,
                                                         @Valid @RequestBody PagoMembresiaRequest request) {
        return ApiResponse.success(membresiaService.actualizarPago(admin.gimnasioId(), id, pagoId, request));
    }
}
