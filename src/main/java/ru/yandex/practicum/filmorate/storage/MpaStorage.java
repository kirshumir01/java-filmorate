package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> getAll();

    Optional<Mpa> get(int id);
}
