package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.StorageData;

import java.util.List;

public interface Storage<T extends StorageData> {
    List<T> getAll();

    T get(Long id);

    T create(T data);

    T update(T data);

    void delete(Long id);
}
