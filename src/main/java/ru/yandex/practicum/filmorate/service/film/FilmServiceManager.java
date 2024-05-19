package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmServiceManager implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmServiceManager(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film get(Long id) {
        if (id == null) {
            throw new NotFoundException("Идентификатор фильма отсутствует");
        }

        return filmStorage.get(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        checkFilmId(filmStorage.get(newFilm.getId()));

        return filmStorage.update(newFilm);
    }

    @Override
    public void delete(Long id) {
        filmStorage.delete(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        checkFilmId(filmStorage.get(filmId));
        checkUserId(userStorage.get(userId));

        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId)  {
        checkFilmId(filmStorage.get(filmId));
        checkUserId(userStorage.get(userId));

        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private void checkFilmId(Film film) {
        if (film == null) {
            throw new NotFoundException("Информация о фильме отсутствует");
        }

        if (film.getId() == null) {
            throw new NotFoundException("Идентификатор фильма" + film.getName() + " не задан");
        }

        if (!filmStorage.getAll().contains(film)) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    private void checkUserId(User user) {
        if (user == null) {
            throw new NotFoundException("Информация о пользователе отсутствует");
        }

        if (user.getId() == null) {
            throw new NotFoundException("Идентификатор пользователя " + user.getLogin() + " не задан");
        }

        if (!userStorage.getAll().contains(user)) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }
}
