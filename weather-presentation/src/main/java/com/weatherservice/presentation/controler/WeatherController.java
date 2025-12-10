package com.weatherservice.presentation.controler;

import com.weatherservice.application.service.WeatherService;
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
import javax.validation.constraints.NotBlank;

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

    @Operation(
            summary = "Get weather by city",
            description = "Retrieves current weather information for a specific city"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data"),
            @ApiResponse(responseCode = "400", description = "Invalid city name"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "503", description = "Weather service unavailable")
    })
    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponse> getWeather(
            @PathVariable @NotBlank String city,
            @RequestParam(required = false, defaultValue = "false") boolean fresh) {

        log.info("Weather request for city: {}, fresh: {}", city, fresh);

        com.weatherservice.domain.model.Weather weather;

        if (fresh) {
            weather = weatherService.getWeather(city);
            return ResponseEntity.ok(WeatherMapper.toResponse(weather, false));
        } else {
            weather = weatherService.getCachedWeather(city);
            boolean fromCache = weather.getTimestamp()
                    .isAfter(java.time.LocalDateTime.now().minusMinutes(9));
            return ResponseEntity.ok(WeatherMapper.toResponse(weather, fromCache));
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