FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
WORKDIR /app
COPY target/leave-tracker-0.0.1.jar leave-tracker-0.0.1.jar
EXPOSE 8005
CMD ["java", "-jar", "leave-tracker-0.0.1.jar"]
