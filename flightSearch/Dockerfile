# Use an OpenJDK base image with Java 21
FROM eclipse-temurin:21-jdk-alpine as builder

# Set working directory inside the container
WORKDIR /app

# Copy Gradle files and source code
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY src ./src

# Build the application using Gradle
RUN ./gradlew bootJar --no-daemon

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port 8080 (default for Spring Boot)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
