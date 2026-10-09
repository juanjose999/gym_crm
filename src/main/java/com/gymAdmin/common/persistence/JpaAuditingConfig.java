package com.gymAdmin.common.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Separado de la clase principal para que los tests por capas (p. ej. @WebMvcTest) no requieran JPA.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
