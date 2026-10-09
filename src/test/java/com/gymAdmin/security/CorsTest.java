package com.gymAdmin.security;

import com.gymAdmin.plan.PlanController;
import com.gymAdmin.plan.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlanController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQtZGUtMzItYnl0ZXMtcGFyYS1obWFjISE=",
        "jwt.access-token-expiration=15m",
        "jwt.refresh-token-expiration=7d",
        "cors.allowed-origins=https://mi-front.com"
})
class CorsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private PlanService planService;

    @Test
    void preflightDeOrigenPermitidoNoExigeToken() throws Exception {
        mockMvc.perform(options("/planes")
                        .header("Origin", "https://mi-front.com")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://mi-front.com"));
    }

    @Test
    void origenNoPermitidoEsRechazado() throws Exception {
        mockMvc.perform(options("/planes")
                        .header("Origin", "https://sitio-malicioso.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
