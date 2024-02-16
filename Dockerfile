# Используйте официальный образ Gradle как базовый
FROM gradle:8.5-jdk-alpine AS build

# Установите рабочую директорию в /app
WORKDIR /app1-comments

# Скопируйте ваш проект Gradle в рабочую директорию /app внутри контейнера
COPY . /app1-comments

# Соберите приложение с помощью Gradle
RUN gradle build --no-daemon

# Используйте официальный образ OpenJDK для запуска приложения
FROM openjdk:17-alpine3.14

# Скопируйте собранный JAR-файл в образ
COPY --from=build /app1-comments/build/libs/comments-project-0.0.1-SNAPSHOT.jar /app.jar

# Запустите приложение при запуске контейнера
CMD ["java", "-jar", "/app.jar"]
