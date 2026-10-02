package com.gymAdmin.controller;

import com.gymAdmin.entity.Usuario;
import com.gymAdmin.service.dtos.UsuarioRequestDto;
import com.gymAdmin.service.UsuarioService;
import com.gymAdmin.service.dtos.UsuarioResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ResponseCustom> save(@RequestBody UsuarioRequestDto requestDTO) {
        UsuarioResponseDto savedUser = usuarioService.save(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseCustom.success(savedUser)
        );
    }

    @GetMapping
    public ResponseEntity<ResponseCustom> findByEmail(@RequestParam
                                                          @NotBlank(message = "El correo no puede estar vacío")
                                                          @Email(message = "Debe proporcionar un formato de correo válido")
                                                          String email) {
        UsuarioResponseDto userfind = usuarioService.findByEmail(email);
        return ResponseEntity.ok(
                ResponseCustom.success(userfind)
        );
    }

    @DeleteMapping
    public ResponseEntity<ResponseCustom> deleteByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                ResponseCustom.success(null)
        );
    }
}