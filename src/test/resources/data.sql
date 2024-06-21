MERGE INTO genres (id, name)
KEY(id)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO mpa_ratings (id, name)
KEY(id)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

DELETE FROM users;
DELETE FROM films;

INSERT INTO users (id, email, login, name, birthday)
VALUES (1, 'user1@yandex.ru', 'user1', 'User1 Name', '1990-03-24');

INSERT INTO users (id, email, login, name, birthday)
VALUES (2, 'user2@yandex.ru', 'user2', 'User2 Name', '1990-03-25');

INSERT INTO users (id, email, login, name, birthday)
VALUES (3, 'user3@yandex.ru', 'user3', 'User3 Name', '1990-03-26');

INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (1,'Test film 1', 'Film1 description', '2007-05-31', 135, '3');

INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (2,'Test film 2', 'Film2 description', '2000-01-31', 120, '1');

INSERT INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (3,'Test film 3', 'Film3 description', '2003-04-15', 60, '5');