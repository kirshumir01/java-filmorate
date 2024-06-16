package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> get(int id);

    LinkedHashSet<Genre> getGenresOfFilm(long filmId);
}
