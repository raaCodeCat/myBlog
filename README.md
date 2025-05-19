# Блог-платформа на Spring Framework

## Описание проекта

Это веб-приложение блога, разработанное с использованием Spring Framework 6.1+ и Java 21. Приложение предоставляет функционал для создания и управления постами, комментариями и лайками.

## Основной функционал

- Лента постов с превью (название, картинка, краткое содержание)
- Фильтрация по тегам и пагинация
- Страница поста с полным содержанием, комментариями и лайками
- CRUD операции для постов и комментариев

## Технологии

### Основной стек
- **Java 21**
- **Spring Boot 3.4.5**
    - spring-boot-starter-web
    - spring-boot-starter-data-jdbc
    - spring-boot-starter-thymeleaf
    - spring-boot-starter-test
- **База данных**: H2 (in-memory)
- **Шаблонизатор**: Thymeleaf 3.1.2
- **Система сборки**: Gradle

### Тестирование
- Spring Boot Test

### Схема базы данных
![img.png](data_base_scheme.png)

## Запуск проекта

### Требования
- JDK 21
- Gradle 8

### Сборка и запуск
Клонировать репозиторий:
```
git clone https://github.com/raaCodeCat/myBlog.git
```

Собрать проект:
```
./gradlew bootJar
```
Файл будет создан в: build/libs/myBlog-0.0.1-SNAPSHOT.jar

Запустить приложение:
```
java -jar build/libs/myBlog-0.0.1-SNAPSHOT.jar
```
После запуска приложение будет достепно по аддресу
```
http://localhost:8080/
```