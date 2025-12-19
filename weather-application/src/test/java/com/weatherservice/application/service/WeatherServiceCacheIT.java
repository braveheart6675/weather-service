package com.weatherservice.application.service;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WeatherServiceCacheIT {

    @Autowired
    private WeatherServiceImpl weatherService;

    @MockBean
    private WeatherClient weatherClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("weather").clear();
    }

    @Test
    void should_cache_weather_result() {
        // given
        String city = "tehran";

        Weather weather = Weather.builder()
                .city(city)
                .temperature(25.0)
                .description("Sunny")
                .timestamp(LocalDateTime.now())
                .build();

        when(weatherClient.fetchWeather(city)).thenReturn(weather);

        // when - first call (CACHE MISS)
        Weather firstCall = weatherService.getCachedWeather(city);

        // when - second call (CACHE HIT)
        Weather secondCall = weatherService.getCachedWeather(city);

        // then
        verify(weatherClient, times(1)).fetchWeather(city);

        assertThat(firstCall).isEqualTo(secondCall);
        assertThat(secondCall.getCity()).isEqualTo(city);
    }
}
