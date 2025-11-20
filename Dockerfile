FROM gradle:8.14.3-jdk21-ubi AS build

WORKDIR /app

COPY *.gradle ./
COPY gradle ./gradle

COPY src ./src

RUN gradle clean build --build-cache -x test

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]