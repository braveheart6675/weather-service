package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherClient implements WeatherClient {

    private final OpenWeatherFeignClient openWeatherFeignClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.units:metric}")
    private String units;

    @Override
    @Cacheable(value = "weather", key = "#city", unless = "#result == null")
    @CircuitBreaker(name = "weatherService", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "weatherService", fallbackMethod = "retryFallback")
    public Weather fetchWeather(String city) {
        log.info("Fetching weather from OpenWeather API for city: {}", city);

        try {
            OpenWeatherResponse response = openWeatherFeignClient.getWeather(city, apiKey, units);
            log.debug("Successfully fetched weather for city: {}", city);
            return mapToDomain(response);
        } catch (FeignException e) {
            log.error("Feign error fetching weather for city: {}. Status: {}", city, e.status(), e);
            throw new RuntimeException("Failed to fetch weather from OpenWeather API", e);
        }
    }

    @Override
    public Weather getFallbackWeather(String city) {
        log.warn("Using fallback weather data for city: {}", city);

        return Weather.builder()
                .city(city)
                .country("Unknown")
                .temperature(20.0)
                .feelsLike(20.0)
                .humidity(50)
                .pressure(1013)
                .description("Weather data temporarily unavailable")
                .icon("02d")
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Fallback برای Circuit Breaker
    public Weather circuitBreakerFallback(String city, Throwable throwable) {
        log.warn("Circuit breaker triggered for city: {}. Using fallback data. Cause: {}",
                city, throwable.getMessage());
        return getFallbackWeather(city);
    }

    // Fallback برای Retry
    public Weather retryFallback(String city, Throwable throwable) {
        log.warn("All retry attempts failed for city: {}. Using fallback data. Cause: {}",
                city, throwable.getMessage());
        return getFallbackWeather(city);
    }

    private Weather mapToDomain(OpenWeatherResponse response) {
        if (response == null || response.getWeather() == null || response.getWeather().isEmpty()) {
            log.warn("Invalid response from OpenWeather API");
            return getFallbackWeather(response != null ? response.getName() : "Unknown");
        }

        return Weather.builder()
                .city(response.getName())
                .country(response.getSys() != null ? response.getSys().getCountry() : "Unknown")
                .temperature(response.getMain().getTemp())
                .feelsLike(response.getMain().getFeelsLike())
                .humidity(response.getMain().getHumidity())
                .pressure(response.getMain().getPressure())
                .description(response.getWeather().get(0).getDescription())
                .icon(response.getWeather().get(0).getIcon())
                .timestamp(LocalDateTime.now())
                .build();
    }
}