# Filmorate

Представьте, что вы решили отдохнуть и провести вечер за просмотром фильма. Вкусная еда уже готовится, ваш любимый плед уютно свернулся в кресле — а вы все еще не выбрали, что посмотреть!

Фильмов много и с каждым годом их становится все больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. Однако сейчас не время сдаваться! Filmorate — сервис, который работает с фильмами и оценками пользователей, предлагает список наиболее популярных фильмов, рекомендованных к просмотру. Теперь ни вам, ни вашим друзьям не придется долго думать, что посмотреть вечером.

## Описание
___
Сервис Filmorate предназначен для хранения информации о фильмах, пользователях и отзывах к фильмам.
<br>

### Основные функции:
- **фильмы:** 
  - действия: добавление, обновление, хранение, получение и удаление информации (название, описание, дата релиза, продолжительность, рейтинг MPAA, жанры,  количество оценок пользователей);
  - функционал: назначение и удаление "лайков" пользователями, получение пользователем списка наиболее популярных фильмов;
- **пользователи:**
  - действия: добавление, обновление, хранение, получение и удаление информации (имя пользователя, логин, адрес электронной почты, дата рождения);
  - функционал: добавление в друзья, удаление из друзей, получение полного списка друзей, получение списка общих друзей пользователей;
- **отзывы к фильмам:**
  - добавление, обновление, хранение, получение и удаление информации (содержание, оценка);
  - функционал: назначение и удаление "лайков" и "дизлайков" пользователями.

## Схема базы данных приложения (ER-диаграмма)

---
![ER-diagram](https://github.com/kirshumir01/java-filmorate/blob/add-database/java-filmorate-diagram.png?raw=true)

## Используемые технологии

---
- **Фреймворк:** Spring Boot
- **База данных:** H2
- **Подключение к базе данных:** Spring Data (JDBC)
- **Тестирование:** JUnit

## Инструкция по запуску приложения

---

### Инструменты для запуска приложения

Убедитесь, что в используемой операционной системе установлено:
- Java Development Kit (JDK) 21 or later;
- Apache Maven 3.8.8.

### Подготовка к запуску приложения

1. Склонируйте репозиторий с использованием следующей команды в терминале (командной строке) операционной системы:

   `git clone https://github.com/kirshumir01/java-filmorate.git`

2. Перейдите в корневую директорию склонированного проекта:

   `cd java-filmorate`

3. Запустите сборку проекта с использованием Maven:

   `./mvnw clean install`

### Запуск приложения

Запустите приложение с использованием следующей команды:

`./mvnw spring-boot:run`

Приложение запустится на порту `8080`. Доступ к публичному API будет открыт по ссылке `http://localhost:8080`.
