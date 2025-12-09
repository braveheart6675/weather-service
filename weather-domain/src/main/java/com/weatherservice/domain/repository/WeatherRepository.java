package com.weatherservice.domain.repository;

import com.weatherservice.domain.model.Weather;
import java.util.Optional;

public interface WeatherRepository {
    Optional<Weather> findByCity(String city);
    Weather save(String city, Weather weather);
    void deleteByCity(String city);
    boolean existsByCity(String city);
}