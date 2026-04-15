FROM eclipse-temurin:21-jre-alpine
LABEL authors="egorm"

WORKDIR /app
COPY target/email-service-0.0.1-SNAPSHOT.jar /app/email.jar
EXPOSE 5050
ENTRYPOINT ["java", "-jar", "email.jar"]