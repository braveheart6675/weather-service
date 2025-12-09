package com.weatherservice.application.service;

import com.weatherservice.domain.model.Weather;
import com.weatherservice.domain.repository.WeatherRepository;
import com.weatherservice.domain.exception.WeatherException;
import com.weatherservice.application.port.input.WeatherUseCase;
import com.weatherservice.application.port.output.WeatherClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService, WeatherUseCase {

    private static final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherRepository weatherRepository;
    private final WeatherClient weatherClient;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherClient weatherClient) {
        this.weatherRepository = weatherRepository;
        this.weatherClient = weatherClient;
    }

    @Override
    public Weather getWeather(String city) {
        log.info("Fetching fresh weather data for city: {}", city);

        try {
            Weather weather = weatherClient.fetchWeather(city);
            return weatherRepository.save(city, weather);
        } catch (Exception e) {
            log.error("Failed to fetch weather for city: {}", city, e);
            throw new WeatherException("Failed to fetch weather for city: " + city, e);
        }
    }

    @Override
    public Weather getCachedWeather(String city) {
        log.debug("Getting cached weather for city: {}", city);

        return weatherRepository.findByCity(city)
                .filter(w -> !w.isStale())
                .orElseGet(() -> getWeather(city));
    }

    @Override
    public void clearCache(String city) {
        log.info("Clearing cache for city: {}", city);
        weatherRepository.deleteByCity(city);
    }
}