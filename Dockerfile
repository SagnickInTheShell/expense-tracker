# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy Maven configuration files first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the application (make sure we're in /app)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]