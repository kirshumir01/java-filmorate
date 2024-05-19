package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

public interface FilmService {
    List<Film> getAll();

    Film get(Long id);

    Film create(Film film);

    Film update(Film newFilm);

    void delete(Long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopular(int count);
}
