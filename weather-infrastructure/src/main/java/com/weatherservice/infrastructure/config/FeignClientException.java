package com.weatherservice.infrastructure.config;

import org.springframework.http.HttpStatus;

public class FeignClientException extends RuntimeException {
    private final HttpStatus status;

    public FeignClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FeignClientException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}