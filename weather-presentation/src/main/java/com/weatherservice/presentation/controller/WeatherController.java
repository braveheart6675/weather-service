package com.weatherservice.presentation.controller;

import com.weatherservice.application.service.WeatherService;
import com.weatherservice.domain.model.Weather;
import com.weatherservice.presentation.dto.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.weatherservice.presentation.mapper.WeatherMapper;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Weather information API")
public class WeatherController {

    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponse> getWeather(
            @PathVariable @NotBlank String city,
            @RequestParam(required = false, defaultValue = "false") boolean fresh) {

        log.info("Weather request for city: {}, fresh: {}", city, fresh);

        Weather weather;
        if (fresh) {
            weather = weatherService.getWeather(city);
            return ResponseEntity.ok(WeatherMapper.toResponse(weather, false));
        } else {
            weather = weatherService.getCachedWeather(city);
            return ResponseEntity.ok(WeatherMapper.toResponse(weather, true));
        }
    }


    @DeleteMapping("/cache/{city}")
    @Operation(summary = "Clear cache for a city")
    @ApiResponse(responseCode = "204", description = "Cache cleared successfully")
    public ResponseEntity<Void> clearCache(
            @PathVariable
            @NotBlank
            @Parameter(description = "City name")
            String city) {

        log.info("Clearing cache for city: {}", city);
        weatherService.clearCache(city);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Weather Service is running");
    }
}