package com.bfc.BarFitCixSistema.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web adicional para CORS y controladores de vista.
 *
 * @author Tegafadax
 * @version 1.0
 * @since 2025-06-10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configuración CORS para permitir requests desde cualquier origen
     * durante el desarrollo.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    /**
     * Configurar controladores de vista simples.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Opcional: redirigir la raíz a dashboard si lo deseas
        // registry.addViewController("/").setViewName("redirect:/dashboard");
    }
}