package com.weatherservice.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API error response")
public class ApiErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private final int status;

    @Schema(description = "Error message", example = "Invalid request parameters")
    private final String message;

    @Schema(description = "Error details")
    private final Map<String, String> details;

    @Schema(description = "Error timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    @Schema(description = "API path", example = "/api/v1/weather")
    private final String path;

    // Constructor
    private ApiErrorResponse(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.details = builder.details;
        this.timestamp = builder.timestamp;
        this.path = builder.path;
    }

    // Getters
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Map<String, String> getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String message;
        private Map<String, String> details;
        private LocalDateTime timestamp;
        private String path;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(Map<String, String> details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this);
        }
    }
}