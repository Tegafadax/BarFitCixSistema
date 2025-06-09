package com.bfc.BarFitCixSistema.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración temporal para permitir todo acceso sin autenticación.
 * Elimina esta clase o reemplázala cuando actives seguridad real.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF (útil para pruebas con Postman)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite TODAS las rutas sin autenticación
                )
                .httpBasic(Customizer.withDefaults()); // Habilita autenticación básica (no obligatoria aquí)

        return http.build();
    }
}
