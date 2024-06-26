package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.*;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> get(int id);

    Optional<LinkedHashSet<Genre>> getBy(Set<Integer> ids);
}
