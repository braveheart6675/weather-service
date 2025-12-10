# 🌤️ Weather Service

A Spring Boot microservice with Clean Architecture that provides weather information by wrapping OpenWeather API.

## 🏗️ Architecture
- **Clean Architecture** with multi-module Maven
- **Domain Layer**: Core business logic and entities
- **Application Layer**: Use cases and business rules
- **Infrastructure Layer**: External APIs (OpenWeather) and Redis caching
- **Presentation Layer**: REST API with Swagger documentation

## 🚀 Features
- ✅ OpenWeather API integration with Feign Client
- ✅ Redis caching for 10-minute TTL
- ✅ Circuit breaker with Resilience4j
- ✅ Fallback mechanism for API failures
- ✅ REST API with Swagger/OpenAPI
- ✅ Global exception handling
- ✅ Input validation
- ✅ Multi-module Maven structure

## 🛠️ Tech Stack
- Java 11
- Spring Boot 2.7
- Spring Cloud OpenFeign
- Spring Data Redis
- Resilience4j
- Swagger/OpenAPI 3
- Maven
- Docker (آماده)

## 📁 Project Structure
weather-service/
├── weather-domain/ # Core business logic
├── weather-application/ # Use cases
├── weather-infrastructure/ # External integrations
├── weather-presentation/ # REST API layer
└── weather-boot/ # Spring Boot app


## 🔧 Setup & Run
1. Clone repository
2. Get OpenWeather API key from [openweathermap.org](https://openweathermap.org/api)
3. Set environment variable:
   ```bash
   export OPENWEATHER_API_KEY=your_api_key_here

Run Redis:
docker run -p 6379:6379 redis:alpine

Run application:
cd weather-boot
mvn spring-boot:run

📚 API Documentation
After running, visit: http://localhost:8080/swagger-ui.html

Endpoints:
GET /api/v1/weather/{city} - Get weather by city

DELETE /api/v1/weather/cache/{city} - Clear cache for city

GET /api/v1/weather/health - Health check

🐳 Docker
docker-compose up

🧪 Testing
bash
mvn test
📄 License
MIT

text

### **۲. اضافه کردن فایل‌های Docker:**

**`Dockerfile`:**
```dockerfile
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/weather-boot/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

