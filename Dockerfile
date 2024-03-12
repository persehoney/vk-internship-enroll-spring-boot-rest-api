# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Copy Maven files for dependency resolution
COPY pom.xml ./
COPY .mvn .mvn

# Copy application source code
COPY src src

# Build the project and create the executable JAR
RUN mvn clean install -DskipTests

# Stage 2: Run stage
FROM eclipse-temurin:21

# Set working directory
WORKDIR vk-internship-enroll-spring-boot-rest-api

# Copy the JAR file from the build stage
COPY --from=build target/*.jar vk-internship-enroll-spring-boot-rest-api.jar

# Expose port 1221
EXPOSE 1221

# Set the entrypoint command for running the application
ENTRYPOINT ["java", "-jar", "vk-internship-enroll-spring-boot-rest-api.jar"]