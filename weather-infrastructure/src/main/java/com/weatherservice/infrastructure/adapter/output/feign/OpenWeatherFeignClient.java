package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import com.weatherservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "openweather-client",
        url = "${openweather.api.url}",
        configuration = FeignConfig.class
)
public interface OpenWeatherFeignClient {

    @GetMapping("/weather")
    OpenWeatherResponse getWeather(
            @RequestParam("q") String city,
            @RequestParam("appid") String apiKey,
            @RequestParam("units") String units
    );
}