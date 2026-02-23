FROM openjdk:21
LABEL authors="egorm"

WORKDIR /app
ADD maven/email-service-0.0.1-SNAPSHOT.jar /app/email.jar
EXPOSE 5050
ENTRYPOINT ["java", "-jar", "email.jar"]