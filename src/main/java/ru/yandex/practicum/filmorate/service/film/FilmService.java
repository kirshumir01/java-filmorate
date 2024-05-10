package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(Long id) {
        return filmStorage.get(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = get(filmId);
        // проверка, что пользователь существует
        User user = userService.get(userId);
        film.getLikes().add(userId);
        update(film);
        log.info("Пользователь {} поставил лайк к фильму {}", user.getLogin(), film.getName());
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = get(filmId);
        User user = userService.get(userId);
        // проверка, что ранее пользователем был поставлен лайк
        if (!film.getLikes().contains(userId)) {
            log.info("Пользователь {} ранее не ставил лайк к фильму {}", user.getLogin(), film.getName());
            throw new NotFoundException("Пользователь " + user.getLogin() + " ранее не ставил лайк к фильму " +
                    film.getName());
        }
        film.getLikes().remove(userId);
        update(film);
        log.info("Пользователь {} удалил лайк к фильму {}", user.getLogin(), film.getName());
        return film;
    }

    public List<Film> getPopular(int count) {
        List<Film> films = filmStorage.getAll();
        films.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        // Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size());
        // List<Film> popularFilms = films.stream().sorted(comparator.reversed()).limit(count).collect(Collectors.toList());
        log.info("Получена информация о самых популярных фильмах");
        return films.stream().limit(count).collect(Collectors.toList());
    }
}
