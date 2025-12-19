package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherClient implements WeatherClient {

    private final OpenWeatherFeignClient feignClient;

    @Override
    @CircuitBreaker(
            name = "weatherService",
            fallbackMethod = "fallback"
    )
    public Weather fetchWeather(String city) {
        log.info("Calling OpenWeather API for city: {}", city);
        return mapToDomain(feignClient.getWeather(city));
    }

    public Weather fallback(String city, Throwable ex) {
        log.error("🔥 FEIGN FALLBACK city={}", city, ex);

        return Weather.builder()
                .city(city)
                .description("Service unavailable (fallback)")
                .temperature(20.0)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Weather mapToDomain(OpenWeatherResponse response) {
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
