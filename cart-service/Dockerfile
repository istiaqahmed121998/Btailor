# ------------------------------
# Stage 1: Build the service
# ------------------------------
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy Maven configuration first for dependency caching
COPY . .

# Download dependencies (cached in Docker layers)
RUN mvn dependency:go-offline -B

# Build only cart-service and its dependencies
RUN mvn -pl cart-service -am -DskipTests clean package

# ------------------------------
# Stage 2: Runtime image
# ------------------------------
FROM gcr.io/distroless/java21:latest

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/cart-service/target/*.jar app.jar

# Expose the service port
EXPOSE 8084

# Start the service with configurable Spring profile
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]