FROM gradle:8.5-jdk-alpine AS build

WORKDIR /app1-comments

COPY . /app1-comments

RUN gradle build --no-daemon

FROM openjdk:17-alpine3.14

COPY --from=build /app1-comments/build/libs/comments-project-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
