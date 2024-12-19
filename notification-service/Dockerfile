# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app

ARG SERVICE_NAME=notification-service

# Copy only the pom.xml and dependencies first for caching
COPY $SERVICE_NAME/pom.xml .
COPY $SERVICE_NAME/src ./src
COPY common ./common

WORKDIR /app/common
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests

WORKDIR /app

# Download dependencies (cache layer)
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copy the rest of the application and build
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app

ARG SERVICE_NAME=notification-service

# Copy the built JAR file from the builder stage
COPY --from=builder /app/${SERVICE_NAME}/target/*.jar app.jar

EXPOSE 8800

ENTRYPOINT ["java", "-jar", "app.jar"]
