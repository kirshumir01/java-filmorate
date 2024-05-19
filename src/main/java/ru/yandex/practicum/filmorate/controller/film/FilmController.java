package ru.yandex.practicum.filmorate.controller.film;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Запрос информации обо всех фильмах");
        List<Film> films = filmService.getAll();
        log.info("Получена информации обо всех фильмах");
        return films;
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable @Positive long id) {
        log.info("Запрос информации о фильме с идентификатором {}", id);
        Film film = filmService.get(id);
        log.info("Получена информация о фильме {}", filmService.get(id).getName());
        return film;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на сохранение информации о новом фильме {}", film.getName());
        Film createdFilm = filmService.create(film);
        log.info("Информация о новом фильме {} сохранена", film.getName());
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Запрос на обновление информации о фильме {}", newFilm.getName());
        Film updatedFilm = filmService.update(newFilm);
        log.info("Информация фильме {} обновлена", newFilm.getName());
        return updatedFilm;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long id) {
        log.info("Запрос на удаление информации о фильме c идентификатором {}", id);
        filmService.delete(id);
        log.info("Информация о фильме c идентификатором {} удалена", id);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        log.info("Запрос на добавление 'лайка' пользователя с идентификатором {} к фильму c идентификатором {}",
                userId, id);
        filmService.addLike(id, userId);
        log.info("Пользователь с идентификатором {} поставил 'лайк' к фильму {}",
                userId, filmService.get(id).getName());
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        log.info("Запрос на удаление лайка пользователя с идентификатором {} к фильму с идентификатором {}",
                userId, id);
        filmService.deleteLike(id, userId);
        log.info("Пользователя с идентификатором {} удалил 'лайк' к фильму {}",
                userId, filmService.get(id).getName());
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") @Positive final Integer count) {
        log.info("Запрос на получение информации о " + count + " самых популярных фильмах");
        List<Film> popularFilms = filmService.getPopular(count);
        log.info("Информация о " + count + " самых популярных фильмах получена");
        return popularFilms;
    }
}

