package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();

    Optional<Film> get(long id);

    Film create(Film film);

    Film update(Film newFilm);

    void delete(long id);

    List<Film> getPopular(int count);
}
