package com.weatherservice.domain.model;

import lombok.Builder;
import lombok.Value;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder
public class Weather {

    @NotBlank
    String city;

    @NotBlank
    String country;

    @NotNull
    Double temperature;

    @NotNull
    Double feelsLike;

    @NotNull
    @Min(0) @Max(100)
    Integer humidity;

    @NotNull
    @Min(0)
    Integer pressure;

    @NotBlank
    String description;

    String icon;

    @NotNull
    LocalDateTime timestamp;

    public boolean isStale() {
        return timestamp.isBefore(LocalDateTime.now().minusMinutes(10));
    }

    public boolean isValid() {
        return temperature != null
                && humidity != null
                && pressure != null
                && description != null
                && !description.isEmpty();
    }
}