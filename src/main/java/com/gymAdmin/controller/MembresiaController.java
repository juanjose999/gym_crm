package com.gymAdmin.controller;

import com.gymAdmin.entity.Membresia;
import com.gymAdmin.service.MembresiaService;
import com.gymAdmin.service.dtos.MembresiaRequestDto;
import com.gymAdmin.service.dtos.MembresiaResponseDto;
import com.gymAdmin.service.dtos.PagoMembresiaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;

    @PostMapping
    public ResponseEntity<ResponseCustom> create(@RequestBody MembresiaRequestDto membresia) {
        return ResponseEntity.ok(
                ResponseCustom.success(Map.of("membresia", membresiaService.save(membresia)))
        );
    }

    @PostMapping("/{membresiaId}/pagos")
    public ResponseEntity<ResponseCustom> createPayment(
            @PathVariable Integer membresiaId,
            @RequestBody @Valid PagoMembresiaRequest requestDto) {

        return ResponseEntity.ok(ResponseCustom.success(
                membresiaService.savePayment(membresiaId, requestDto)
        ));
    }

    @GetMapping
    public ResponseEntity<ResponseCustom> findAll() {
        return ResponseEntity.ok(
                ResponseCustom.success(Map.of("membresias", membresiaService.findAll()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCustom> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                ResponseCustom.success(Map.of("membresia", membresiaService.findById(id)))
        );
    }

    @PutMapping("/{membresiaId}/pagos/{pagoId}")
    public ResponseEntity<ResponseCustom> updatePayment(
            @PathVariable Integer membresiaId,
            @PathVariable Long pagoId,
            @RequestBody PagoMembresiaRequest requestDto) {

        return ResponseEntity.ok(
                ResponseCustom.success(Map.of("membresia",membresiaService.updatePayment(membresiaId, pagoId, requestDto)))
        );
    }

    @PutMapping("/{id}/")
    public ResponseEntity<ResponseCustom> updateById(@PathVariable Integer id, @RequestBody MembresiaRequestDto membresia) {
        return ResponseEntity.ok(
                ResponseCustom.success(Map.of("membresia",membresiaService.update(membresia, id)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        membresiaService.delete(id);
        return ResponseEntity.ok().build();
    }
}
