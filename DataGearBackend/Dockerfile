# Stage 1: Build JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Build the project (creates target/app.jar)
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:21-jdk-slim
WORKDIR /app

# Install netcat for healthcheck
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Copy the built jar from previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]