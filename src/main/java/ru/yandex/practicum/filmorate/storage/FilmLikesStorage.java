package ru.yandex.practicum.filmorate.storage;

public interface FilmLikesStorage {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}
