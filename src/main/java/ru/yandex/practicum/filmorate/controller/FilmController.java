package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private long currentId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(++currentId);
        films.put(film.getId(), film);
        log.info("Информация о новом фильме сохранена");
        return film;
    }

    @PutMapping
    public Film update(@Valid @Validated @RequestBody Film newFilm) {
        checkFilmId(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Информация о фильме с идентификатором " + newFilm.getId() + " обновлена");
        return newFilm;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получена информация о всех сохраненных фильмах");
        return new ArrayList<>(films.values());
    }

    private void checkFilmId(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Ошибка поиска фильма по идентификатору: " + film.getId());
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
    }
}

