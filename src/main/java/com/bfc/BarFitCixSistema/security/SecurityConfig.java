package com.bfc.BarFitCixSistema.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para desactivar la autenticación durante el desarrollo.
 * IMPORTANTE: Esta configuración debe cambiarse en producción.
 *
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-10
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración del filtro de seguridad que permite acceso a todas las rutas
     * sin autenticación.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactivar CSRF (Cross-Site Request Forgery) para APIs REST
                .csrf(csrf -> csrf.disable())

                // Configurar las autorizaciones de requests
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso a todos los endpoints sin autenticación
                        .requestMatchers("/**").permitAll()
                        // Cualquier otra request también se permite
                        .anyRequest().permitAll()
                )

                // Configurar manejo de sesiones
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Desactivar el formulario de login por defecto
                .formLogin(form -> form.disable())

                // Desactivar la autenticación HTTP básica
                .httpBasic(basic -> basic.disable())

                // Desactivar logout
                .logout(logout -> logout.disable());

        return http.build();
    }
}