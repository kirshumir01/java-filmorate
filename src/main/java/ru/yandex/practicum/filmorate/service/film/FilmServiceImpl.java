package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final FilmLikesStorage filmLikesStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film get(long id) {
        return filmStorage.get(id).orElseThrow(
                () -> new NotFoundException("Фильм с id = " + id + " не найден"));
    }

    @Override
    public Film create(Film film) {
        checkMpa(film);
        checkGenres(film);
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
        filmLikesStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId)  {
        checkFilmId(filmId);
        checkUserId(userId);
        filmLikesStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private void checkFilmId(long filmId) {
        if (filmStorage.get(filmId).isEmpty()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    private void checkUserId(long userId) {
        if (userStorage.get(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private void checkMpa(Film film) {
        if (mpaStorage.get(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Информация о рейтинге с id = " + film.getMpa().getId() + " отсутствует.");
        }
    }

    private void checkGenres(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genreStorage.get(genre.getId()).isEmpty()) {
                    throw new ValidationException("Информация о жанре с id = " + genre.getId() + " отсутствует.");
                }
            }
        }
    }
}
