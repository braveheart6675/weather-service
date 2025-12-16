package com.weatherservice.application.service;

import com.weatherservice.domain.model.Weather;

public interface WeatherService {

    Weather getWeather(String city);

    Weather getCachedWeather(String city);

    void clearCache(String city);
}
