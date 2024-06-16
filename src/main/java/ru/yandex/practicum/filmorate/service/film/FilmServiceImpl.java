package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film get(long id) {
        checkFilmId(id);
        return filmStorage.get(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        checkFilmId(newFilm.getId());
        return filmStorage.update(newFilm);
    }

    @Override
    public void delete(long id) {
        filmStorage.delete(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId)  {
        checkFilmId(filmId);
        checkUserId(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private void checkFilmId(Long id) {
        if (filmStorage.get(id) == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    private void checkUserId(Long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
