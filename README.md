# Filmorate

### Описание

___
Сервис для хранения информации о фильмах и пользователях.
<br>
#### Основные функции:
- добавление, обновление, хранение, получение и удаление информации о фильмах
(название, описание, дата релиза, продолжительность, рейтинг MPAA, жанры,
количество оценок пользователей);
- добавление, обновление, хранение, получение и удаление информации о пользователях
(имя пользователя, логин, адрес электронной почты, дата рождения);
- назначение и удаление "лайков" фильмов пользователями;
- добавление и удаление информация о друзьях и о статусах дружбы пользователей.
***

### Схема базы данных приложения (ER-диаграмма)

---
![ER-diagram](https://github.com/kirshumir01/java-filmorate/blob/add-database/java-filmorate-diagram.png?raw=true)

### Хранение информации о фильмах, жанрах, рейтингах и "лайках"

---
1. Таблица **films** содержит 7 граф:
- *id* - идентификатор фильма;
- *name* - название фильма;
- *description* - описание;
- *release_date* - дата релиза;
- *duration* - продолжительность;
- *mpa_rating_id* - рейтинг фильма по версии MPA;
- *genre_id* - идентификатор жанра.
2. Наименование рейтинга содержатся в таблице **mpa_ratings**, связь с которой обеспечивается 
по идентификатору рейтинга, хранящегося в таблице **films** (*mpa_rating_id*).
3. Наименования жанров содержатся в таблице **genres**, связь с которой обеспечивается
через промежуточную таблицу **films_genres** по идентификаторам фильма (*film_id*) и жанра (*genre_id*).
4. Информация о "лайках" пользователей содержится в таблице **films_likes**,
которая хранит в себе связи между идентификаторами фильмов (*film_id*) и пользователей (*user_id*).

##### Жанры фильмов
1. Комедия.
2. Драма.
3. Мультфильм.
4. Триллер.
5. Документальный.
6. Боевик.

##### Рейтинги фильмов
1. G — у фильма нет возрастных ограничений.
2. PG — детям рекомендуется смотреть фильм с родителями.
3. PG-13 — детям до 13 лет просмотр не желателен.
4. R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого.
5. NC-17 — лицам до 18 лет просмотр запрещён.


### Хранение информации о пользователях, дружбе и статусах дружбы

---
1. Таблица **users** содержит 5 граф:
- *id* - идентификатор пользователя;
- *email* - адрес электронной почты;
- *login* - логин;
- *name* - имя пользователя;
- *birthday* - дата рождения.
2. Информация о друзьях пользователя содержится в таблице **friendship**,
которая хранит в себе связи между идентификаторами пользователей (*user_id*, *friend_id*)
и статус дружбы (*friendship_status*): *true* - заявка пользователя на добавление в друзья подтверждена,
*false* - заявка не подтверждена.

### Примеры запросов к базе данных

---

<details>

<summary>Примеры запросов для операций с фильмами</summary>

#### addFilm():
```SQL
INSERT INTO films (
    name, description, release_date, duration, mpa_rating_id
    )
VALUES ('film', 'description', '2020-10-10', 120, 3);
```

#### updateFilmById():
```SQL
UPDATE films SET
    name = 'name',
    description = 'description',
    release_date = '2020-10-10',
    duration = 120,
    mpa_rating_id = 3
    WHERE id = 1;
```

#### getAll():
```SQL
SELECT id, name, description, release_date, duration, mpa_rating_id
FROM films;
```
#### getFilmById():
```SQL
SELECT id, name, description, release_date, duration, mpa_rating_id
FROM films
WHERE id = 1;
```
#### deleteFilmById():
```SQL
DELETE FROM films WHERE id = 1;
```
#### getGenresByFilmId():
```SQL
SELECT id, name
FROM genres WHERE id IN (
    SELECT genre_id FROM films_genres
    WHERE film_id = 1
    );
```

</details>

<br>

<details>

<summary>Примеры запросов для операций с пользователями</summary>

#### addUser():
```SQL
INSERT INTO users (
    email, login, name, birthday
    )
VALUES ('email@ya,ru', 'login', 'name', '1990-10-10');
```

#### updateUserById():
```SQL
UPDATE users SET
    email = 'email.ya.ru',
    login = 'login',
    name = 'name',
    birthday = '1990-10-10';
    WHERE id = 1;
```

#### getAll():
```SQL
SELECT id, login, name, email, birthday, friend_id
FROM users;
```
#### getUserById():
```SQL
SELECT id, login, name, email, birthday, friend_id
FROM users
WHERE id = 1;
```
#### deleteUserById():
```SQL
DELETE FROM users WHERE id = 1;
```
</details>