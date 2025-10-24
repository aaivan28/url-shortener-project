FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurim:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar url-shortener-rest-api.jar
EXPOSE 8080
