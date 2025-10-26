package com.hospital.hospitalapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI hospitalOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Hospital Management System API")
                .description("Complete HMS with AI Prescription Intelligence")
                .version("1.0")
                .contact(new Contact()
                    .name("Hospital API Support")
                    .email("support@hospital.com")));
    }
}
