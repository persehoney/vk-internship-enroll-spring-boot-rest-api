FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY pom.xml ./
COPY .mvn .mvn

COPY src src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR vk-internship-enroll-spring-boot-rest-api

COPY --from=build target/*.jar vk-internship-enroll-spring-boot-rest-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "vk-internship-enroll-spring-boot-rest-api.jar"]