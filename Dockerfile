FROM amazoncorretto:17.0.7-alpine

# Установка рабочего каталога
WORKDIR /app

# Копирование файла JAR в контейнер
COPY target/Skidoz-1.jar app.jar

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]