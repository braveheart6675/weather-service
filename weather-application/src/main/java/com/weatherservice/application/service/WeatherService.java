package com.weatherservice.application.service;

import com.weatherservice.domain.model.Weather;

public interface WeatherService {

    /**
     * Fetch fresh weather data from external API
     */
    Weather getWeather(String city);

    /**
     * Get weather data with cache-first strategy
     */
    Weather getCachedWeather(String city);

    /**
     * Clear cache for specific city
     */
    void clearCache(String city);
}