package ru.yandex.practicum.filmorate.controller.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Запрос информации обо всех рейтингах фильмов");
        List<Mpa> filmsMpaRatings = mpaService.getAllMpaRatings();
        log.info("Получена информации обо всех рейтингах фильмов");
        return filmsMpaRatings;
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable int id) {
        log.info("Запрос информации о рейтинге с идентификатором {}", id);
        Mpa mpaFilmRating = mpaService.getMpaRating(id);
        log.info("Получена информация о рейтинге {}", mpaService.getMpaRating(id).getName());
        return mpaFilmRating;
    }
}
