package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

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

    List<Genre> getAllGenres();

    Genre getGenre(int id);

    List<Mpa> getAllMpaRatings();

    Mpa getMpaRating(int id);



}
