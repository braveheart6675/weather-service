package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import feign.FeignException;
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

    @Value("${openweather.api.units:metric}")
    private String units;

    @Override
    @Cacheable(value = "weather", key = "#city", unless = "#result == null")
    public Weather fetchWeather(String city) {
        log.info("Fetching weather from OpenWeather API for city: {}", city);

        try {
            OpenWeatherResponse response = openWeatherFeignClient.getWeather(city, units);
            return mapToDomain(response);
        } catch (FeignException e) {
            log.error("Error fetching weather from OpenWeather API for city: {}", city, e);
            return getFallbackWeather(city);
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

    private Weather mapToDomain(OpenWeatherResponse response) {
        return Weather.builder()
                .city(response.getName())
                .country(response.getSys().getCountry())
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