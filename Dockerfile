#BUILD
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

#RUN
FROM eclipse-temurin:17-jre
WORKDIR /app

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker \
    MAIL_API_KEY="" \
    MAIL_API_URL="" \
    H2_USERNAME="" \
    H2_PASSWORD="" \

COPY --from=build /app/target/*.jar /app/app.jar

VOLUME ["/data"]

ENTRYPOINT ["java", "-jar", "/app/app.jar"]