package com.weatherservice.application.port.output;

import com.weatherservice.domain.model.Weather;

/**
 * Port defining the output (external services) of the application
 */
public interface WeatherClient {
    Weather fetchWeather(String city);
}