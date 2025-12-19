# Weather Service 🌤️

A clean, resilient, Redis-cached Weather API built with Spring Boot 3.4,  
OpenFeign, and Resilience4j.

A production-ready Spring Boot 3.4 / Java 21 Weather Service built with Clean Architecture, using OpenFeign to integrate with the OpenWeather API, Redis for caching, and Resilience4j for fault tolerance (Circuit Breaker & Fallback).

---

## ✨ Features

- Clean Architecture (Domain / Application / Infrastructure / Presentation)
- Java 21 & Spring Boot 3.4
- OpenFeign client for external API integration
- Redis-based caching (Spring Cache abstraction)
- Resilience4j Circuit Breaker with fallback
- REST API for weather data by city
- Actuator endpoints for monitoring
- Docker & Docker Compose ready
- Unit-tested service layer

---

## 🏗️ Architecture

```text
weather-service
├── weather-domain         # Core domain models & business rules
├── weather-application    # Use cases & service layer
├── weather-infrastructure # Feign clients, Redis, external adapters
├── weather-presentation   # REST controllers
└── weather-boot           # Spring Boot application (entry point)

This project strictly follows Clean Architecture principles:

Domain layer has no dependency on Spring

Infrastructure depends on Application, not vice versa

External systems (Redis, OpenWeather) are replaceable

🔌 External API

This service integrates with the OpenWeather API.

Required configuration
openweather:
  api:
    key: YOUR_API_KEY
    url: https://api.openweathermap.org/data/2.5
    units: metric

🚀 REST API
Get weather by city (cached)
GET /api/weather?city=London

Force fresh data (bypass cache)
GET /api/weather?city=London&fresh=true

Clear cache for a city
DELETE /api/weather/cache?city=London

🧠 Caching (Redis)

Redis is used as the cache provider

Cache key format: weather::{city}

TTL: 10 minutes

Cache applied at service level using Spring Cache

@Cacheable(value = "weather", key = "#city.toLowerCase()")
public Weather getCachedWeather(String city)

🛡️ Fault Tolerance & Fallback

The service uses Resilience4j Circuit Breaker.

Circuit Breaker name: weatherService

Applied at service layer

Automatic fallback when:

OpenWeather API is unavailable

Network errors occur

Circuit is OPEN

@CircuitBreaker(
    name = "weatherService",
    fallbackMethod = "fallback"
)

Fallback behavior

When triggered, a safe default Weather response is returned:

description: "Service unavailable (fallback)"

temperature: 20.0

🔍 Monitoring & Actuator

Enabled Actuator endpoints:

GET /actuator/health
GET /actuator/metrics
GET /actuator/circuitbreakers


Circuit Breaker state can be monitored in real time.

🧪 Testing

Service layer unit tests are provided.

Example:

WeatherServiceImplTest

Run tests:

mvn test

🐳 Docker Support
Build image
docker build -t weather-service .

Run container
docker run -p 8080:8080 weather-service

🐳 Docker Compose (with Redis)
docker-compose up -d


Services:

weather-service

redis

⚙️ Tech Stack

Java 21

Spring Boot 3.4

Spring Cloud OpenFeign

Spring Cache

Redis (Lettuce)

Resilience4j

Maven (multi-module)

Docker / Docker Compose

📦 Build
mvn clean package


The final runnable JAR is produced by the weather-boot module.

📌 Notes

Redis must be running for caching to work

API keys should never be committed (use environment variables in production)

Designed for easy extension (new providers, new APIs)

👤 Author

Developed as a clean, production-grade Spring Boot example,
focused on architecture, resilience, and real-world patterns
