package com.weatherservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Weather Service API",
                version = "1.0.0",
                description = "API documentation for Weather Service Application",
                contact = @Contact(
                        name = "Support Team",
                        email = "support@weatherservice.com",
                        url = "https://weatherservice.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "https://weatherservice.com/terms"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8081",
                        description = "Local Development Server"
                ),
                @Server(
                        url = "https://api.weatherservice.com",
                        description = "Production Server"
                )
        },
        tags = {
                @Tag(name = "Weather", description = "Weather data endpoints"),
                @Tag(name = "Forecast", description = "Weather forecast endpoints")
        }
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**") // فقط endpointهای /api/ را نشان بده
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/admin/**") // endpointهای admin جداگانه
                .build();
    }
}