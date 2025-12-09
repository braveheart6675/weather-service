package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "openWeatherClient",
        url = "${openweather.api.url}",
        configuration = com.weatherservice.infrastructure.config.FeignConfig.class,
        fallback = OpenWeatherFallback.class
)
public interface OpenWeatherFeignClient {

    @GetMapping("/weather")
    OpenWeatherResponse getWeather(
            @RequestParam("q") String city,
            @RequestParam("units") String units
    );
}