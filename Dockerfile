# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom files first (better layer caching)
COPY pom.xml .
COPY weather-domain/pom.xml weather-domain/pom.xml
COPY weather-application/pom.xml weather-application/pom.xml
COPY weather-infrastructure/pom.xml weather-infrastructure/pom.xml
COPY weather-presentation/pom.xml weather-presentation/pom.xml
COPY weather-boot/pom.xml weather-boot/pom.xml

# Download dependencies
RUN mvn -B -q dependency:go-offline

# Copy source code
COPY . .

# Build Spring Boot jar
RUN mvn -B -q clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/weather-boot/target/weather-boot-*.jar app.jar

# Expose application port
EXPOSE 8080

# JVM options for container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
