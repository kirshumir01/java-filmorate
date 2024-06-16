package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final FilmLikesStorage filmLikesStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final UserStorage userStorage;

    @Override
    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        films.forEach(film -> film.setGenres(genreStorage.getGenresOfFilm(film.getId())));
        return films;
    }

    @Override
    public Film get(long id) {
        Film film = filmStorage.get(id).orElseThrow(
                () -> new NotFoundException("Фильм с id = " + id + " не найден"));
        film.setGenres(genreStorage.getGenresOfFilm(film.getId()));
        return film;
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
        checkMpa(newFilm);
        checkGenres(newFilm);
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
        filmLikesStorage.checkLike(filmId, userId);
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

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenre(int id) {
        return genreStorage.get(id).orElseThrow(() -> new NotFoundException("Жанр c id = " + id + " не найден."));
    }

    @Override
    public List<Mpa> getAllMpaRatings() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getMpaRating(int id) {
        return mpaStorage.get(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден."));
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
