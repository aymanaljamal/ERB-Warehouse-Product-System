FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY demo/ .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
# ضغط المحتوى عند الاستجابة
ENV SERVER_COMPRESSION_ENABLED=true
ENV SERVER_COMPRESSION_MIME_TYPES=application/json,application/xml,text/html,text/xml,text/plain

ENTRYPOINT ["java", "-jar", "app.jar"]
