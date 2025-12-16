package com.weatherservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                if (inputStream != null && inputStream.available() > 0) {
                    byte[] bodyBytes = inputStream.readAllBytes();
                    String responseBody = new String(bodyBytes);

                    log.error("Feign client error - Method: {}, Status: {}, Body: {}",
                            methodKey, response.status(), responseBody);

                    if (responseBody.contains("\"cod\"")) {
                        try {
                            OpenWeatherError error = objectMapper.readValue(responseBody, OpenWeatherError.class);
                            return new FeignClientException(
                                    "OpenWeather API error: " + error.getMessage(),
                                    HttpStatus.valueOf(response.status())
                            );
                        } catch (IOException e) {
                        }
                    }
                }
            }

            // Handle specific HTTP status codes
            if (response.status() == 400) {
                return new FeignClientException("Bad request to external API", HttpStatus.BAD_REQUEST);
            } else if (response.status() == 401) {
                return new FeignClientException("Unauthorized access to external API", HttpStatus.UNAUTHORIZED);
            } else if (response.status() == 404) {
                return new FeignClientException("City not found in external API", HttpStatus.NOT_FOUND);
            } else if (response.status() >= 500) {
                return new FeignClientException("External API server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IOException e) {
            log.error("Error reading feign response body", e);
        }

        // Fallback to default decoder
        return defaultErrorDecoder.decode(methodKey, response);
    }

    // Inner class for OpenWeather error response
    private static class OpenWeatherError {
        private String cod;
        private String message;

        public String getCod() { return cod; }
        public void setCod(String cod) { this.cod = cod; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}