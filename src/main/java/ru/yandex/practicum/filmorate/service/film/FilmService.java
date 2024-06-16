package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

public interface FilmService {
    List<Film> getAll();

    Film get(long id);

    Film create(Film film);

    Film update(Film newFilm);

    void delete(long id);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getPopular(int count);
}
