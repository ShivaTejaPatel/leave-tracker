#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY target/leave-tracker-0.0.1.jar leave-tracker-0.0.1.jar
#CMD ["java", "-jar", "leave-tracker-0.0.1.jar"]





# Build stage
FROM eclipse-temurin:17.0.14_7-jdk-ubi9-minimal AS builder
WORKDIR /build
COPY target/leave-tracker-0.0.1.jar app.jar
# Extract layers
RUN java -Djarmode=layertools -jar app.jar extract

# Run stage
FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
WORKDIR /app

# Copy layers from builder
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

# Configure JVM options
#ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC"


ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]