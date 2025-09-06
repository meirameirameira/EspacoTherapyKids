package org.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiSecurityConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("UUID"); // apenas informativo na doc
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", bearer))
                // aplica "bearerAuth" globalmente (todos os endpoints mostram Authorize)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
