MERGE INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (1,'Test film 1', 'Film1 description', '2007-05-31', 135, '3');

MERGE INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (2,'Test film 2', 'Film2 description', '2000-01-31', 120, '1');

MERGE INTO films (id, name, description, release_date, duration, mpa_rating_id)
VALUES (3,'Test film 3', 'Film3 description', '2003-04-15', 60, '5');