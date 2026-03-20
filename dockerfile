# -------- Build stage --------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy entire project to make go-offline work
COPY . .

# Download dependencies
RUN mvn -B -e -DskipTests dependency:go-offline

# Build the application
RUN mvn clean package -DskipTests

# -------- Runtime stage --------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy jar from builder
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]