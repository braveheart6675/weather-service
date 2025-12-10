package com.weatherservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
        info = @Info(title = "Weather Service API", version = "v1"),
        tags = {
        }
)
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class WeatherServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherServiceApplication.class, args);
    }
}

