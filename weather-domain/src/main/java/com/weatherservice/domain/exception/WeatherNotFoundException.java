package com.weatherservice.domain.exception;

public class WeatherNotFoundException extends WeatherException {

    public WeatherNotFoundException(String city) {
        super("Weather data not found for city: " + city);
    }
}