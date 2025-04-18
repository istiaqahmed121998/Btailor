# Stage 1: Build the app using Maven
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy the pom.xml and download dependencies first (cached layer)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copy the rest of the source and build the app
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar api-gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]