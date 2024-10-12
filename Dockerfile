# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/OmegaPoisk-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
