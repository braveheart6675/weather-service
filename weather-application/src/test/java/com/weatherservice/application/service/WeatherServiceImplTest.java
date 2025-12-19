package com.weatherservice.application.service;

import com.weatherservice.application.port.output.WeatherClient;
import com.weatherservice.domain.model.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    void getCachedWeather_shouldReturnWeatherFromClient() {
        // given
        String city = "tehran";
        Weather weather = Weather.builder()
                .city("Tehran")
                .temperature(22.0)
                .build();

        when(weatherClient.fetchWeather(city)).thenReturn(weather);

        // when
        Weather result = weatherService.getCachedWeather(city);

        // then
        assertEquals("Tehran", result.getCity());
        assertEquals(22.0, result.getTemperature());
        verify(weatherClient).fetchWeather(city);
    }

    @Test
    void getWeather_shouldReturnFreshWeather() {
        // given
        String city = "karaj";
        Weather weather = Weather.builder()
                .city("Karaj")
                .temperature(18.5)
                .build();

        when(weatherClient.fetchWeather(city)).thenReturn(weather);

        // when
        Weather result = weatherService.getWeather(city);

        // then
        assertEquals("Karaj", result.getCity());
        verify(weatherClient).fetchWeather(city);
    }
}
