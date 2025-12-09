package com.weatherservice.infrastructure.adapter.output.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenWeatherResponse {
    private String name;
    private Sys sys;
    private Main main;
    private List<Weather> weather;

    @Data
    public static class Sys {
        private String country;
    }

    @Data
    public static class Main {
        private Double temp;

        @JsonProperty("feels_like")
        private Double feelsLike;

        private Integer humidity;
        private Integer pressure;
    }

    @Data
    public static class Weather {
        private String description;
        private String icon;
    }
}