package com.gymAdmin.service;

import com.gymAdmin.entity.Membresia;
import com.gymAdmin.entity.PagoMembresia;
import com.gymAdmin.entity.Plan;
import com.gymAdmin.entity.Usuario;
import com.gymAdmin.repository.MembresiaRepositoy;
import com.gymAdmin.repository.PagoMembresiaRepository;
import com.gymAdmin.repository.PlanRepository;
import com.gymAdmin.repository.UsuarioRepository;
import com.gymAdmin.service.dtos.MembresiaRequestDto;
import com.gymAdmin.service.dtos.MembresiaResponseDto;
import com.gymAdmin.service.dtos.PagoMembresiaRequest;
import com.gymAdmin.service.mappers.MembresiaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembresiaService {

    private final MembresiaRepositoy membresiaRepositoy;
    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;
    private final PagoMembresiaRepository pagoMembresiaRepository;

    public MembresiaResponseDto save(MembresiaRequestDto membresiaDto) {

        Usuario usuario = getUsuario(membresiaDto.usuarioId());
        Plan plan = getPlan(membresiaDto.planId());

        Membresia membresia = MembresiaMapper.toEntity(membresiaDto, usuario, plan);
        membresiaRepositoy.save(membresia);

        PagoMembresia pagoMembresia = builderPagoMembresia(membresiaDto.monto(), membresiaDto.metodoDePago());
        pagoMembresia.setMembresia(membresia);
        pagoMembresiaRepository.save(pagoMembresia);

        membresia.setPagos(List.of(pagoMembresia));

        return MembresiaMapper.toMembresiaDto(membresia);
    }

    public MembresiaResponseDto savePayment(Integer membresiaId, PagoMembresiaRequest pagoMembresiaRequest) {

        Membresia membresiaFinded = getMembresia(membresiaId);

        PagoMembresia pagoMembresia = builderPagoMembresia( pagoMembresiaRequest.monto(), pagoMembresiaRequest.metodo_pago());
        pagoMembresia.setMembresia(membresiaFinded);
        pagoMembresiaRepository.save(pagoMembresia);

        List<PagoMembresia> pagoMembresias = membresiaFinded.getPagos();
        pagoMembresias.add(pagoMembresia);

        membresiaFinded.setPagos(pagoMembresias);
        membresiaRepositoy.save(membresiaFinded);

        return MembresiaMapper.toMembresiaDto(membresiaFinded);

    }

    public MembresiaResponseDto updatePayment(Integer membresiaId, Long pagoId,
                                              PagoMembresiaRequest pagoMembresiaRequest) {
        Membresia membresiaFinded = getMembresia(membresiaId);

        PagoMembresia pagoMembresiaExistente = pagoMembresiaRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago membresia no encontrado"));
        pagoMembresiaExistente.setMetodo_pago(pagoMembresiaRequest.metodo_pago());
        pagoMembresiaExistente.setMonto(pagoMembresiaRequest.monto());
        pagoMembresiaRepository.save(pagoMembresiaExistente);


        return MembresiaMapper.toMembresiaDto(membresiaFinded);

    }

    public Membresia findById(Integer id) {
        return getMembresia(id);
    }

    public List<MembresiaResponseDto> findAll() {
        return membresiaRepositoy.findAll().stream().map(MembresiaMapper::toMembresiaDto).collect(Collectors.toList());
    }

    public MembresiaResponseDto update(MembresiaRequestDto membresiaDto, Integer id) {
        Membresia membresiaFinded = getMembresia(id);

        Usuario usuario = getUsuario(membresiaDto.usuarioId());
        Plan plan = getPlan(membresiaDto.planId());
        List<PagoMembresia> pagosExistentesMembria = membresiaFinded.getPagos();
        PagoMembresia pagoNuevoMembresia = builderPagoMembresia(membresiaDto.monto(), membresiaDto.metodoDePago());
        pagosExistentesMembria.add(pagoNuevoMembresia);
        Membresia membresia = MembresiaMapper.toEntity(membresiaDto, usuario, plan);
        membresia.setPagos(List.of(pagoNuevoMembresia));
        membresia.setId(membresiaFinded.getId());
        membresiaRepositoy.save(membresia);

        return MembresiaMapper.toMembresiaDto(membresia);
    }

    public void delete(Integer id) {
        Membresia membresia = membresiaRepositoy.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada"));
        membresiaRepositoy.delete(membresia);
    }


    private  Usuario getUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        return usuario;
    }

    private Plan getPlan(Long id) {

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado con ID: " + id));
        return plan;
    }

    private final Membresia getMembresia(Integer id) {
        return membresiaRepositoy.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripcion no encontrada"));
    }
    private PagoMembresia builderPagoMembresia( Double monto, String metodoDePago) {
        PagoMembresia pagoMembresia = new PagoMembresia();
        pagoMembresia.setMonto(monto);
        pagoMembresia.setMetodo_pago(metodoDePago);
        return pagoMembresia;
    }


}
