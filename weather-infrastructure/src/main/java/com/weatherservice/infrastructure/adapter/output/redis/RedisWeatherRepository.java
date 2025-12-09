package com.weatherservice.infrastructure.adapter.output.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherservice.domain.model.Weather;
import com.weatherservice.domain.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisWeatherRepository implements WeatherRepository {

    private static final String KEY_PREFIX = "weather:";
    private static final long TTL_MINUTES = 10;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Weather> findByCity(String city) {
        try {
            String key = KEY_PREFIX + city.toLowerCase();
            Object value = redisTemplate.opsForValue().get(key);

            if (value == null) {
                return Optional.empty();
            }

            Weather weather = objectMapper.convertValue(value, Weather.class);
            return Optional.of(weather);
        } catch (Exception e) {
            log.error("Error retrieving weather from cache for city: {}", city, e);
            return Optional.empty();
        }
    }

    @Override
    public Weather save(String city, Weather weather) {
        try {
            String key = KEY_PREFIX + city.toLowerCase();
            redisTemplate.opsForValue().set(key, weather, TTL_MINUTES, TimeUnit.MINUTES);
            log.debug("Weather data cached for city: {}", city);
            return weather;
        } catch (Exception e) {
            log.error("Error caching weather for city: {}", city, e);
            return weather;
        }
    }

    @Override
    public void deleteByCity(String city) {
        String key = KEY_PREFIX + city.toLowerCase();
        redisTemplate.delete(key);
        log.debug("Cache cleared for city: {}", city);
    }

    @Override
    public boolean existsByCity(String city) {
        String key = KEY_PREFIX + city.toLowerCase();
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}