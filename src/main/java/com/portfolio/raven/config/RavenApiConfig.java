package com.portfolio.raven.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RavenApiConfig {

    @Bean
    public OpenAPI ravenOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Raven API")
                        .version("1.0.0")
                        .description("REST API for the Raven music portfolio project.")
                        .contact(new Contact()
                                .name("Edu Braga")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
