package com.weatherservice.infrastructure.adapter.output.feign;

import com.weatherservice.infrastructure.adapter.output.feign.dto.OpenWeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherFallback implements OpenWeatherFeignClient {

    @Override
    public OpenWeatherResponse getWeather(String city, String units) {
        log.warn("OpenWeather API is unavailable. Using fallback for city: {}", city);

        OpenWeatherResponse response = new OpenWeatherResponse();
        response.setName(city);

        OpenWeatherResponse.Sys sys = new OpenWeatherResponse.Sys();
        sys.setCountry("Unknown");
        response.setSys(sys);

        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(20.0);
        main.setFeelsLike(20.0);
        main.setHumidity(50);
        main.setPressure(1013);
        response.setMain(main);

        OpenWeatherResponse.Weather weather = new OpenWeatherResponse.Weather();
        weather.setDescription("Service temporarily unavailable");
        weather.setIcon("02d");
        response.setWeather(Collections.singletonList(weather));

        return response;
    }
}