package com.weatherservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Weather information response")
public class WeatherResponse {

    @Schema(description = "City name", example = "Tehran")
    private final String city;

    @Schema(description = "Country code", example = "IR")
    private final String country;

    @Schema(description = "Temperature in Celsius", example = "25.5")
    private final Double temperature;

    @Schema(description = "Feels like temperature in Celsius", example = "26.0")
    private final Double feelsLike;

    @Schema(description = "Humidity percentage", example = "65")
    private final Integer humidity;

    @Schema(description = "Pressure in hPa", example = "1013")
    private final Integer pressure;

    @Schema(description = "Weather description", example = "clear sky")
    private final String description;

    @Schema(description = "Weather icon code", example = "01d")
    private final String icon;

    @Schema(description = "Data timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    @Schema(description = "Indicates if data is from cache")
    private final Boolean cached;

    // Constructor
    private WeatherResponse(Builder builder) {
        this.city = builder.city;
        this.country = builder.country;
        this.temperature = builder.temperature;
        this.feelsLike = builder.feelsLike;
        this.humidity = builder.humidity;
        this.pressure = builder.pressure;
        this.description = builder.description;
        this.icon = builder.icon;
        this.timestamp = builder.timestamp;
        this.cached = builder.cached;
    }

    // Getters
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public Double getTemperature() { return temperature; }
    public Double getFeelsLike() { return feelsLike; }
    public Integer getHumidity() { return humidity; }
    public Integer getPressure() { return pressure; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Boolean getCached() { return cached; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String city;
        private String country;
        private Double temperature;
        private Double feelsLike;
        private Integer humidity;
        private Integer pressure;
        private String description;
        private String icon;
        private LocalDateTime timestamp;
        private Boolean cached;

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder feelsLike(Double feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public Builder humidity(Integer humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder pressure(Integer pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder cached(Boolean cached) {
            this.cached = cached;
            return this;
        }

        public WeatherResponse build() {
            return new WeatherResponse(this);
        }
    }
}