# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only the pom.xml and dependencies first for caching
COPY pom.xml .
COPY src ./src

# Download dependencies (cache layer)
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copy the rest of the application and build
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8800

ENTRYPOINT ["java", "-jar", "app.jar"]
