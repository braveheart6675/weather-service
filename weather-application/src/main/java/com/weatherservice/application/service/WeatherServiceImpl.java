package com.weatherservice.application.service;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherClient weatherClient;

    @Override
    @Cacheable(value = "weather", key = "#city.toLowerCase()")
    public Weather getCachedWeather(String city) {
        log.info("Cache MISS → calling weather client for city: {}", city);
        return weatherClient.fetchWeather(city);
    }

    @Override
    public Weather getWeather(String city) {
        log.info("Fetching fresh weather data for city: {}", city);
        return weatherClient.fetchWeather(city);
    }

    @Override
    @CacheEvict(value = "weather", key = "#city.toLowerCase()")
    public void clearCache(String city) {
        log.info("Cache cleared for city: {}", city);
    }
}
