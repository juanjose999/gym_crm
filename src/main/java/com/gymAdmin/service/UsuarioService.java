package com.gymAdmin.service;

import com.gymAdmin.entity.Usuario;
import com.gymAdmin.exception.ResourceNotFoundException;
import com.gymAdmin.repository.UsuarioRepository;
import com.gymAdmin.service.dtos.UsuarioRequestDto;
import com.gymAdmin.service.dtos.UsuarioResponseDto;
import com.gymAdmin.service.mappers.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDto save(UsuarioRequestDto requestDto) {
        Usuario toEntity = UsuarioMapper.toEntity(requestDto);
        Usuario savedUser = usuarioRepository.save(toEntity);
        return UsuarioMapper.toResponse(savedUser);
    }

    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> UsuarioMapper.toResponse(u))
                .collect(Collectors.toList());
    }


    public UsuarioResponseDto findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el correo: " + email));

        return UsuarioMapper.toResponse(usuario);
    }


    public void deleteByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        usuarioRepository.delete(usuario.get());
    }

}
