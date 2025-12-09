package com.weatherservice.application.port.input;

import com.weatherservice.domain.model.Weather;

/**
 * Port defining the input (use cases) of the application
 */
public interface WeatherUseCase {
    Weather getWeather(String city);
    Weather getCachedWeather(String city);
    void clearCache(String city);
}