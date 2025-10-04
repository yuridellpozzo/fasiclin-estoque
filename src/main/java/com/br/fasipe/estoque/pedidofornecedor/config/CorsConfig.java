package com.br.fasipe.estoque.pedidofornecedor.config;

import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// A anotação @Configuration diz ao Spring para carregar esta classe como configuração
@Configuration
public class CorsConfig {
    
    // O @Bean registra a configuração CORS no contêiner do Spring
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            // Adicionar a anotação @NonNull aqui
            public void addCorsMappings(@NonNull CorsRegistry registry) { 
                registry.addMapping("/api/**") 
                    .allowedOrigins("*") 
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }
}
