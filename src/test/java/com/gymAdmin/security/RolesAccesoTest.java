package com.gymAdmin.security;

import com.gymAdmin.membresia.MembresiaService;
import com.gymAdmin.micuenta.MiCuentaController;
import com.gymAdmin.orden.OrdenService;
import com.gymAdmin.plan.PlanController;
import com.gymAdmin.plan.PlanService;
import com.gymAdmin.usuario.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MiCuentaController.class, PlanController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQtZGUtMzItYnl0ZXMtcGFyYS1obWFjISE=",
        "jwt.access-token-expiration=15m",
        "jwt.refresh-token-expiration=7d"
})
class RolesAccesoTest {

    private static final UsuarioAutenticado ADMIN = new UsuarioAutenticado(1L, "admin@gym.com", "x", Rol.ADMIN, 10L);
    private static final UsuarioAutenticado SOCIO = new UsuarioAutenticado(2L, "socio@gym.com", null, Rol.SOCIO, 10L);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private MembresiaService membresiaService;
    @MockitoBean
    private OrdenService ordenService;
    @MockitoBean
    private PlanService planService;

    @Test
    void sinTokenRecibe401() throws Exception {
        mockMvc.perform(get("/mi-cuenta/membresias")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/planes")).andExpect(status().isUnauthorized());
    }

    @Test
    void socioVeSoloSusDatosTomandoElIdDelToken() throws Exception {
        when(membresiaService.listarPorSocio(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/mi-cuenta/membresias").with(user(SOCIO))).andExpect(status().isOk());

        verify(membresiaService).listarPorSocio(SOCIO.id());
    }

    @Test
    void socioNoPuedeUsarEndpointsDeAdmin() throws Exception {
        mockMvc.perform(get("/planes").with(user(SOCIO))).andExpect(status().isForbidden());
    }

    @Test
    void adminVeLosDatosDeSuGimnasio() throws Exception {
        when(planService.listar(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/planes").with(user(ADMIN))).andExpect(status().isOk());

        verify(planService).listar(ADMIN.gimnasioId());
    }

    @Test
    void adminNoUsaElPortalDeSocios() throws Exception {
        mockMvc.perform(get("/mi-cuenta/compras").with(user(ADMIN))).andExpect(status().isForbidden());
    }
}
