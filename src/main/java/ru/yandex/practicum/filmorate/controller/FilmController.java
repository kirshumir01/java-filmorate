package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final int MAX_DESCRIPTION_SIZE = 200;

    private final LocalDate FIRST_CINEMA_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Информация о новом фильме сохранена");
        return film;
    }

    @PutMapping
    public Film updateFilmData(@Valid @RequestBody Film newFilm) {
        validate(newFilm);
        findFilmById(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Информация о фильме с идентификатором " + newFilm.getId() + " обновлена");
        return newFilm;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получена информация о всех сохраненных фильмах");
        return new ArrayList<>(films.values());
    }

    public void findFilmById(Film film) {
        if (film.getId() == null) {
            log.error("Идентификатор фильма отсутствует");
            throw new ValidationException("Идентификатор фильма должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            log.error("Ошибка поиска фильма по идентификатору: " + film.getId());
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");        }
    }

    public void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.error("Название фильма введено некорректно");
            throw new ValidationException("Название фильма не должно быть пустым");
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_SIZE) {
            log.error("Описание фильма больше 200 символов");
            throw new ValidationException("Описание должно содержать не более 200 символов");
        }

        if (film.getReleaseDate().isBefore(FIRST_CINEMA_RELEASE_DATE) ) {
            log.error("Дата релиза фильма введена некорректно");
            throw new ValidationException("Дата релиза должна быть не раньше " +
                    FIRST_CINEMA_RELEASE_DATE.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма введена некорректно");
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

