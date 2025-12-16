package com.weatherservice.application.service;

import com.weatherservice.application.port.input.WeatherUseCase;
import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.exception.WeatherException;
import com.weatherservice.domain.model.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService, WeatherUseCase {

    private final WeatherClient weatherClient;

    /**
     * همیشه دیتا تازه (بدون کش)
     */
    @Override
    public Weather getWeather(String city) {
        log.info("Fetching fresh weather data for city: {}", city);
        try {
            return weatherClient.fetchWeather(city);
        } catch (Exception e) {
            log.error("Failed to fetch weather for city: {}", city, e);
            throw new WeatherException("Failed to fetch weather for city: " + city, e);
        }
    }

    /**
     * cache-first (Redis via Spring Cache)
     */
    @Override
    @Cacheable(
            value = "weather",
            key = "#city.toLowerCase()"
    )
    public Weather getCachedWeather(String city) {
        log.debug("Cache MISS for city: {}", city);
        return getWeather(city);
    }

    /**
     * حذف کش
     */
    @Override
    @CacheEvict(
            value = "weather",
            key = "#city.toLowerCase()"
    )
    public void clearCache(String city) {
        log.info("Clearing cache for city: {}", city);
    }
}
