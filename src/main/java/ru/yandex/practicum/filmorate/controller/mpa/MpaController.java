package ru.yandex.practicum.filmorate.controller.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Запрос информации обо всех рейтингах фильмов");
        List<Mpa> filmsMpaRatings = filmService.getAllMpaRatings();
        log.info("Получена информации обо всех рейтингах фильмов");
        return filmsMpaRatings;
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable int id) {
        log.info("Запрос информации о рейтинге с идентификатором {}", id);
        Mpa mpaFilmRating = filmService.getMpaRating(id);
        log.info("Получена информация о рейтинге {}", filmService.getMpaRating(id).getName());
        return mpaFilmRating;
    }
}
