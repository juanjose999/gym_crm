package com.gymAdmin.security;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlanController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQtZGUtMzItYnl0ZXMtcGFyYS1obWFjISE=",
        "jwt.access-token-expiration=15m",
        "jwt.refresh-token-expiration=7d",
        "rate-limit.auth.capacity=2",
        "rate-limit.auth.period=1h",
        "rate-limit.api.capacity=2",
        "rate-limit.api.period=1h"
})
class RateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private PlanService planService;

    @Test
    void authSuperaElLimitePorIpRecibe429() throws Exception {
        mockMvc.perform(post("/auth/login")).andExpect(header().string("X-RateLimit-Remaining", "1"));
        mockMvc.perform(post("/auth/login")).andExpect(header().string("X-RateLimit-Remaining", "0"));

        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void elLimiteDeLaApiEsPorUsuario() throws Exception {
        UsuarioAutenticado admin = new UsuarioAutenticado(1L, "a@gym.com", "x", Rol.ADMIN, 10L);
        UsuarioAutenticado otroAdmin = new UsuarioAutenticado(2L, "b@gym.com", "x", Rol.ADMIN, 20L);

        mockMvc.perform(get("/planes").with(user(admin))).andExpect(status().isOk());
        mockMvc.perform(get("/planes").with(user(admin))).andExpect(status().isOk());
        mockMvc.perform(get("/planes").with(user(admin))).andExpect(status().isTooManyRequests());

        mockMvc.perform(get("/planes").with(user(otroAdmin))).andExpect(status().isOk());
    }

    @Test
    void respondeConCabecerasDeSeguridad() throws Exception {
        mockMvc.perform(get("/planes"))
                .andExpect(header().string("Content-Security-Policy", "default-src 'none'; frame-ancestors 'none'"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("Referrer-Policy", "no-referrer"));
    }
}
