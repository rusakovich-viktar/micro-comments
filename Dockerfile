FROM openjdk:17-alpine3.14
COPY /build/libs/comments-project-0.0.1.jar /micro-comments.jar
CMD ["java", "-jar", "/micro-comments.jar"]