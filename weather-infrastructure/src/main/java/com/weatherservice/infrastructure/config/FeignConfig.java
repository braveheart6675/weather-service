package com.weatherservice.infrastructure.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.units}")
    private String units;

    /**
     * Adds OpenWeather required query params globally
     */
    @Bean
    public RequestInterceptor openWeatherRequestInterceptor() {
        return template -> {
            template.query("appid", apiKey);
            template.query("units", units);
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
