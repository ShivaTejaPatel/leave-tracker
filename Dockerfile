FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/leave-tracker-0.0.1.jar leave-tracker-0.0.1.jar


CMD ["java", "-jar", "leave-tracker-0.0.1.jar"]