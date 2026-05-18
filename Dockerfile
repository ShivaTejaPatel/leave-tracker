FROM eclipse-temurin:17-jre-ubi9-minimal

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app


COPY --chown=spring:spring target/leave-tracker.jar leave-tracker.jar

# Switch to non-root user
USER spring:spring

EXPOSE 8005


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar leave-tracker.jar"]