package com.weatherservice.presentation.mapper;

import com.weatherservice.domain.model.Weather;
import com.weatherservice.presentation.dto.WeatherResponse;
import java.time.LocalDateTime;

public class WeatherMapper {

    public static WeatherResponse toResponse(Weather weather, boolean fromCache) {
        return WeatherResponse.builder()
                .city(weather.getCity())
                .country(weather.getCountry())
                .temperature(weather.getTemperature())
                .feelsLike(weather.getFeelsLike())
                .humidity(weather.getHumidity())
                .pressure(weather.getPressure())
                .description(weather.getDescription())
                .icon(weather.getIcon())
                .timestamp(weather.getTimestamp())
                .cached(fromCache)
                .build();
    }
}