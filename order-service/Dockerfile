# ------------------------------
# Stage 1: Build the service
# ------------------------------
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set working directory for the build
WORKDIR /build

# Copy Maven wrapper and configs
COPY mvnw .
RUN chmod +x mvnw  #  Critical fix
COPY .mvn .mvn
COPY pom.xml .

# Copy the entire monorepo (safe if .dockerignore excludes target folders)
COPY . ./

# Build only this service and its dependencies (e.g., common-library)
RUN mvn -pl order-service -am -DskipTests clean install
# ------------------------------
# Stage 2: Create runtime image
# ------------------------------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /build

# Copy built JAR from builder
COPY --from=builder /build/order-service/target/*.jar app.jar

# Expose the service port (adjust per service)
EXPOSE 8086

# Start the service with profile from environment
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]
