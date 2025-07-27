# ------------------------------
# Stage 1: Build the service
# ------------------------------
# Use a specific Maven version for reproducible builds
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /build

# --- Source Code Layer ---
# For a monorepo, copy the entire project context first.
# This ensures that when Maven builds its project "reactor", it can see all
# the modules and their pom.xml files from the very beginning.
COPY . .

# Make the Maven Wrapper executable
# Check if mvnw exists before trying to make it executable
RUN if [ -f mvnw ]; then chmod +x mvnw; fi

# --- Build Layer ---
# Build the specific service and its dependencies ('-am').
# Using 'package' is sufficient as we only need the JAR for the next stage.
# This single command downloads dependencies and compiles the code. Docker caches
# this layer, so it will be fast if no source files have changed.
RUN mvn -pl product-service -am -DskipTests clean install

# ------------------------------
# Stage 2: Create the final, minimal runtime image
# ------------------------------
# Use a distroless image for a smaller and more secure final image.
FROM gcr.io/distroless/java17-debian11


# Set the working directory for the application
WORKDIR /app

# Copy the built JAR from the 'builder' stage into the final image
# Using the specific path to the JAR is more robust than a wildcard.
COPY --from=builder /build/product-service/target/*.jar app.jar

# Expose the port the service runs on
EXPOSE 8082

# Define the command to run the application
# The Spring profile canx be set via an environment variable at runtime.
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]