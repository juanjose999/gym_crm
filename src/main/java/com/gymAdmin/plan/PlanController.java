package com.gymAdmin.plan;

import com.gymAdmin.common.api.ApiResponse;
import com.gymAdmin.plan.dto.PlanRequest;
import com.gymAdmin.plan.dto.PlanResponse;
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
@RequestMapping("/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PlanResponse> crear(@AuthenticationPrincipal UsuarioAutenticado admin,
                                           @Valid @RequestBody PlanRequest request) {
        return ApiResponse.success(planService.crear(admin.gimnasioId(), request));
    }

    @GetMapping
    public ApiResponse<List<PlanResponse>> listar(@AuthenticationPrincipal UsuarioAutenticado admin) {
        return ApiResponse.success(planService.listar(admin.gimnasioId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<PlanResponse> buscarPorId(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                 @PathVariable Long id) {
        return ApiResponse.success(planService.buscarPorId(admin.gimnasioId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<PlanResponse> actualizar(@AuthenticationPrincipal UsuarioAutenticado admin,
                                                @PathVariable Long id,
                                                @Valid @RequestBody PlanRequest request) {
        return ApiResponse.success(planService.actualizar(admin.gimnasioId(), id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@AuthenticationPrincipal UsuarioAutenticado admin, @PathVariable Long id) {
        planService.eliminar(admin.gimnasioId(), id);
    }
}
