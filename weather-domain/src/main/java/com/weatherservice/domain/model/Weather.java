package com.weatherservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {

    private String city;
    private String country;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private int pressure;
    private String description;
    private String icon;
    private LocalDateTime timestamp;
}
