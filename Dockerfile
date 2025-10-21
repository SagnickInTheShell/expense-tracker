# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven wrapper files if they exist
COPY mvnw* ./
COPY .mvn ./.mvn

# Copy Maven files first for better caching
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variable for port
ENV PORT=8080

# Run the application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]