package com.gymAdmin.service.mappers;

import com.gymAdmin.entity.Membresia;
import com.gymAdmin.entity.PagoMembresia;
import com.gymAdmin.entity.Plan;
import com.gymAdmin.entity.Usuario;
import com.gymAdmin.service.dtos.MembresiaRequestDto;
import com.gymAdmin.service.dtos.MembresiaResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MembresiaMapper {

    public static Membresia toEntity(MembresiaRequestDto membresiaDto,
                                     Usuario usuario,
                                     Plan plan) {
        Membresia membresia = new Membresia();
        membresia.setUsuario(usuario);
        membresia.setPlan(plan);
        membresia.setFecha_inicio(membresiaDto.fechaInicio());
        membresia.setFecha_fin(membresiaDto.fechaFin());
        return membresia;
    }

    public static MembresiaResponseDto toMembresiaDto(Membresia membresia) {
        var usuarioDto = new MembresiaResponseDto.UsuarioSummaryDto(
                membresia.getUsuario().getId(),
                membresia.getUsuario().getNombres(),
                membresia.getUsuario().getEmail()
        );
        var planDto = new MembresiaResponseDto.PlanSummaryDto(
                membresia.getPlan().getId(),
                membresia.getPlan().getNombre(),
                membresia.getPlan().getPrecio()
        );

        List<MembresiaResponseDto.PagoMembresiaDto> pagosMembresias = membresia.getPagos().stream()
                .map(m -> new MembresiaResponseDto.PagoMembresiaDto(
                        m.getId(),
                        m.getMonto(),
                        m.getMetodo_pago()
                ))
                .toList();

        MembresiaResponseDto membresiaResponse = new MembresiaResponseDto(
                membresia.getId(),
                usuarioDto,
                planDto,
                pagosMembresias,
                membresia.getFecha_inicio(),
                membresia.getFecha_fin()
        );
        return membresiaResponse;
    }

}
