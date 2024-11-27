FROM openjdk:17.0.2-slim

WORKDIR /app

ARG JAR_FILE=build/libs/app-1.0.jar
ARG SPRING_PROFILE=deployment
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE

COPY $JAR_FILE app.jar

RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser
USER appuser

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/app/app.jar"]